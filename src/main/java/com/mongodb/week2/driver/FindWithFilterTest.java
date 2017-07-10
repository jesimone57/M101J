package com.mongodb.week2.driver;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by jsimone on 10/22/15.
 */
public class FindWithFilterTest {
    public static void main(String[] args) {
        MongoClient client = new MongoClient("localhost");
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> coll = db.getCollection("findWithFilterTest");

        coll.drop();

        // insert all the documents ...
        for (int i=0; i<10; i++) {
            coll.insertOne(new Document()
                    .append("x", new Random().nextInt(2))
                    .append("y", new Random().nextInt(100)));
        }

//        Bson filter = new Document("x", 0)
//                .append("y", new Document("$gt", 10).append("$lt", 90));

        Bson filter = and( eq("x",0), gt("y",10), lt("y",90) );

        List<Document> all = coll.find(filter).into(new ArrayList<Document>());
        for (Document curDoc:  all) {
            System.out.println(curDoc.toJson());
        }


        System.out.println("Count: ");
        long count = coll.count(filter);
        System.out.println(count);
    }
}
