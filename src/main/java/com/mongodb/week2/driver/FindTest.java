package com.mongodb.week2.driver;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsimone on 10/22/15.
 */
public class FindTest {
    public static void main(String[] args) {
        MongoClient client = new MongoClient("localhost");
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> coll = db.getCollection("findTest");

        coll.drop();

        // insert all the documents ...
        for (int i=0; i<10; i++) {
            coll.insertOne(new Document("x", i));
        }


        System.out.println("Find one: ");
        Document first = coll.find().first();
        System.out.println(first.toJson());



        System.out.println("Find all with into: ");
        List<Document> all = coll.find().into(new ArrayList<Document>());
        for (Document curDoc:  all) {
            System.out.println(curDoc.toJson());
        }


        System.out.println("Find all with iteration: ");
        MongoCursor<Document> cursor = coll.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document curDoc = cursor.next();
                System.out.println(curDoc.toJson());
            }
        } finally {
            cursor.close();
        }

        System.out.println("Count: ");
        long count = coll.count();
        System.out.println(count);
    }
}
