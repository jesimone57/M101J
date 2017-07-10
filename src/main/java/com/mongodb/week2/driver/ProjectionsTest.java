package com.mongodb.week2.driver;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Projections.exclude;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

/**
 * Created by jsimone on 10/22/15.
 */
public class ProjectionsTest {
    public static void main(String[] args) {
        MongoClient client = new MongoClient("localhost");
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> coll = db.getCollection("projectionsTest");

        coll.drop();

        // insert all the documents with two random integers ...
        for (int i=0; i<10; i++) {
            coll.insertOne(new Document()
                    .append("x", new Random().nextInt(2))
                    .append("y", new Random().nextInt(100))
                    .append("i", i));
        }

        Bson filter = and( eq("x",0), gt("y",10), lt("y", 90) );

        // using Documents
//        Bson projection = new Document("x", 0);  // exclude x
//        Bson projection = new Document("y", 1)  // include y
//                .append("i", 1)  // include i
//                .append("_id", 0);  // exclude _id

        // using Bson ...
//        Bson projection = Projections.exclude("x");
//        Bson projection = Projections.include("y", "i");
        Bson projection = fields(
                include("y", "i"),
                exclude("_id"));

        List<Document> all = coll.find(filter)
                .projection(projection)
                .into(new ArrayList<Document>());
        for (Document curDoc:  all) {
            System.out.println(curDoc.toJson());
        }


        System.out.println("Count: ");
        long count = coll.count(filter);
        System.out.println(count);
    }
}
