package com.mongodb.week2.driver;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by jsimone on 10/22/15.
 */
public class InsertOne {
    public static void main(String[] args) {
        MongoClient client = new MongoClient("localhost");
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> coll = db.getCollection("insertTest");

        coll.drop();

        Document doc = new Document()
                .append("name", "Frank Brown")
                .append("age", 42)
                .append("aLong", 1L)
                .append("aDouble", 1.1D)
                .append("likes_icecream", false)
                .append("date", new Date())
                .append("objectId", new ObjectId())
                .append("null", null)
                .append("embeddedDoc", new Document("x", 0))
                .append("list", Arrays.asList(1,2,3));

        System.out.println(doc.toJson());

        coll.insertOne(doc);
    }
}
