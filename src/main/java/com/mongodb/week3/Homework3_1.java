package com.mongodb.week3;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Created by jsimone on 10/22/15.
 *
 Write a program in the language of your choice that will remove the lowest homework score for each student.
 Since there is a single document for each student containing an array of scores, you will need to update the scores
 array and remove the homework.

 Remember, just remove a homework score. Don't remove a quiz or an exam!

 Hint/spoiler: With the new schema, this problem is a lot harder and that is sort of the point.
 One way is to find the lowest homework in code and then update the scores array with the low homework pruned.

 after this code runs there will be only 1 homework grade per student.
 the final answer is:

 > db.students.aggregate( { '$unwind' : '$scores' } , { '$group' : { '_id' : '$_id' , 'average' : { $avg : '$scores.score' } } } , { '$sort' : { 'average' : -1 } } , { '$limit' : 1 } )
 { "_id" : 13, "average" : 91.98315917172745 }

 */
public class Homework3_1 {
    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("school");
        MongoCollection<Document> coll = db.getCollection("students");

        Bson filter =  eq("scores.type", "homework") ;
        Bson sort   = and(eq("_id", 1), eq("score", 1));

        MongoCursor<Document> cursor = coll.find(filter).sort(sort).iterator();
        try {
            while (cursor.hasNext()) {
                Document curDoc = cursor.next();
                System.out.println(curDoc.toJson());
                Integer id = (Integer)curDoc.get("_id");
                //System.out.println("--->"+id);

                Double lowest = 100.0D;
                List<Document> scores = (List<Document>) curDoc.get("scores");
                for (Document doc: scores) {
                    String type = (String)doc.get("type");


                    if (type.equals("homework")) {
                        System.out.println(doc.toJson());
                        Double score = (Double)doc.get("score");
                        if (score < lowest) {
                            lowest = score;
                            //System.out.println("lowest is now "+lowest);
                        }

                        //System.out.println("score "+score);
                    }
                }
                System.out.println("lowest homework score for student "+id+"  is "+lowest);

                Document deletedHomework = new Document("type", "homework").append("score", lowest);
                coll.updateOne(eq("_id", id), new Document("$pull", new Document("scores", deletedHomework)));
            }
        } finally {
            cursor.close();
        }


        System.out.println("Count: ");
        long count = coll.count();
        System.out.println(count);
    }
}
