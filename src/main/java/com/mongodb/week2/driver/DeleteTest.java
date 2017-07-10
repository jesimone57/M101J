package com.mongodb.week2.driver;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lt;

/**
 * Created by jsimone on 10/22/15.
 */
public class DeleteTest {
    public static void main(String[] args) {
        MongoClient client = new MongoClient("localhost");
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> coll = db.getCollection("deleteTest");

        coll.drop();

        // insert all the documents ...
        for (int i=0; i<8; i++) {
            coll.insertOne(new Document()
                    .append("_id", i));
        }

        // individual deletion
        coll.deleteOne(eq("_id", 5));

        // many deletion
        coll.deleteMany(lt("_id", 2));



        List<Document> all = coll.find().into(new ArrayList<Document>());
        for (Document curDoc:  all) {
            System.out.println(curDoc.toJson());
        }


        System.out.println("Count: ");
        long count = coll.count();
        System.out.println(count);
    }
}
