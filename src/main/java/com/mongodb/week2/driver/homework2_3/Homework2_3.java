package com.mongodb.week2.driver.homework2_3;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * Created by jsimone on 10/22/15.
 *
 *
 *
 *  Write a program in the language of your choice that will remove the grade of type "homework" with
 *  the lowest score for each student from the dataset that you imported in the previous homework.
 *  Since each document is one grade, it should remove one document per student.

    Hint/spoiler: If you select homework grade-documents, sort by student and then by score,
    you can iterate through and find the lowest score for each student by noticing a change in student id.
    As you notice that change of student_id, remove the document.

    Initially, the collection will have 800 documents.
    To confirm you are on the right track, here are some queries to run after you process the data
    and put it into the grades collection:

    According to the HW notes ... the answer is:

         > db.grades.find().sort({'score':-1}).skip(100).limit(1)
         { "_id" : ObjectId("50906d7fa3c412bb040eb709"), "student_id" : 100, "type" : "homework", "score" : 88.50425479139126 }
         > db.grades.find({},{'student_id':1, 'type':1, 'score':1, '_id':0}).sort({'student_id':1, 'score':1, }).limit(5)
         { "student_id" : 0, "type" : "quiz", "score" : 31.95004496742112 }
         { "student_id" : 0, "type" : "exam", "score" : 54.6535436362647 }
         { "student_id" : 0, "type" : "homework", "score" : 63.98402553675503 }
         { "student_id" : 1, "type" : "homework", "score" : 44.31667452616328 }
         { "student_id" : 1, "type" : "exam", "score" : 74.20010837299897 }
         > db.grades.aggregate({'$group':{'_id':'$student_id', 'average':{$avg:'$score'}}}, {'$sort':{'average':-1}}, {'$limit':1})
         { "_id" : 54, "average" : 96.19488173037341 }

 */
public class Homework2_3 {
    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("students");
        MongoCollection<Document> coll = db.getCollection("grades");


        Bson filter =  eq("type", "homework") ;
        Bson sort   = and(eq("student_id", 1), eq("score", 1));

        // db.grades.find({type: "homework"}).sort({student_id: 1, score: 1})  // lowest score first
        /*
        { "_id" : ObjectId("50906d7fa3c412bb040eb579"), "student_id" : 0, "type" : "homework", "score" : 14.8504576811645 }
        { "_id" : ObjectId("50906d7fa3c412bb040eb57a"), "student_id" : 0, "type" : "homework", "score" : 63.98402553675503 }
        { "_id" : ObjectId("50906d7fa3c412bb040eb57d"), "student_id" : 1, "type" : "homework", "score" : 21.33260810416115 }
        { "_id" : ObjectId("50906d7fa3c412bb040eb57e"), "student_id" : 1, "type" : "homework", "score" : 44.31667452616328 }
        { "_id" : ObjectId("50906d7fa3c412bb040eb581"), "student_id" : 2, "type" : "homework", "score" : 60.9750047106029 }
        { "_id" : ObjectId("50906d7fa3c412bb040eb582"), "student_id" : 2, "type" : "homework", "score" : 97.75889721343528 }
         */

        MongoCursor<Document> cursor = coll.find(filter).sort(sort).iterator();
        try {
            int i=0;
            while (cursor.hasNext()) {
                Document curDoc = cursor.next();
                System.out.println(curDoc.toJson());

                if (i%2==0) {
                    ObjectId id = curDoc.getObjectId("_id");
                    System.out.println("-> is to be deleted "+id+"    "+ i%2);

                    Bson deleteFilter = eq("_id", id);
                    coll.deleteOne(deleteFilter);
                }
                i++;
            }
        } finally {
            cursor.close();
        }


        System.out.println("Count: ");
        long count = coll.count();
        System.out.println(count);
    }
}
