package com.mongodb.week2.driver;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by jsimone on 10/22/15.
 */
public class UpdateTest {
    public static void main(String[] args) {
        MongoClient client = new MongoClient("localhost");
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> coll = db.getCollection("updateTest");

        coll.drop();

        // insert all the documents ...
        for (int i=0; i<8; i++) {
            coll.insertOne(new Document()
                    .append("_id", i)
                    .append("x", i));
        }

        // complete document replacement
//        coll.replaceOne(eq("x", 5), new Document("_id", 5).append("x", 20)
//                .append("update", true));

        // individual field replacement
        coll.updateOne(eq("x", 5), new Document("$set", new Document("x", 20)));

        // upsert example
        coll.updateOne(eq("_id", 9), new Document("$set", new Document("x", 20)),
                new UpdateOptions().upsert(true));

        // many updates example
        coll.updateMany(gte("_id", 5), new Document("$inc", new Document("x", 1)));


        List<Document> all = coll.find().into(new ArrayList<Document>());
        for (Document curDoc:  all) {
            System.out.println(curDoc.toJson());
        }


        System.out.println("Count: ");
        long count = coll.count();
        System.out.println(count);
    }
}
