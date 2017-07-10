package com.mongodb.week2.driver;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.orderBy;

/**
 * Created by jsimone on 10/22/15.
 */
public class FindWithSortSkipLimitTest {
    public static void main(String[] args) {
        MongoClient client = new MongoClient("localhost");
        MongoDatabase db = client.getDatabase("course");
        MongoCollection<Document> coll = db.getCollection("findWithSortSkipLimitTest");

        coll.drop();

        // insert all the documents ...
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                coll.insertOne(new Document()
                        .append("i", i)
                        .append("j", j));
            }
        }

        Bson projection = fields(include("i", "j"), excludeId());

//        Bson sort = new Document("i", 1).append("j", -1); // 1 = asc, -1 = desc
        Bson sort = orderBy(ascending("i"), descending("j"));

        List<Document> all = coll.find()
                .projection(projection)

                .sort(sort)
                .skip(20)
                .limit(50)

                .into(new ArrayList<Document>());
        for (Document curDoc : all) {
            System.out.println(curDoc.toJson());
        }


        System.out.println("Count: ");
        long count = all.size();
        System.out.println(count);
    }
}
