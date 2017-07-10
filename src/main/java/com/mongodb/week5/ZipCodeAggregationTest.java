package com.mongodb.week5;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jsimone on 11/16/15.
 */
public class ZipCodeAggregationTest {
    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("agg");
        MongoCollection<Document> coll = db.getCollection("zips");

        List<Document> pipeline;
        pipeline = Arrays.asList(
                new Document("$group",
                     new Document("_id", "$state")
                        .append("totalPop", new Document("$sum", "$pop"))),
                new Document("$match",
                        new Document("totalPop", new Document("$gte", 10000000)))
                );

        List<Document> results = coll.aggregate(pipeline).into(new ArrayList<Document>());

        for (Document cur : results) {
            System.out.println(cur.toJson());
        }
    }
}
