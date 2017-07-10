package com.mongodb.week2.driver;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.bson.Document;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;

/**
 * Created by jsimone on 10/12/15.
 */
public class HelloWorldMongoDBSparkFreemarkerStyle {
    public static void main(String[] args) {

        MongoClient client = new MongoClient();
        MongoDatabase db = client.getDatabase("course");
        final MongoCollection<Document> coll = db.getCollection("hello");

        coll.drop();  // start clean
        coll.insertOne(new Document("name", "MongoDB"));

        final Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(HelloWorldMongoDBSparkFreemarkerStyle.class, "/");


        Spark.get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) {

                StringWriter writer = new StringWriter();
                try {
                    Template helloTempate = configuration.getTemplate("hello.ftl");

                    Document doc = coll.find().first();  // the one and only document in the db
                    System.out.println(doc.toJson());

                    helloTempate.process(doc, writer);

                } catch (Exception e) {
                    halt(500);
                    e.printStackTrace();
                }

                return writer;
            }
        });
    }
}
