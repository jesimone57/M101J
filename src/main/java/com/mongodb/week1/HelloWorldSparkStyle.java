package com.mongodb.week1;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Created by jsimone on 10/12/15.
 * NOTE: If you run this then go to web browser you can hit http://localhost:4567/
 */
public class HelloWorldSparkStyle {
    public static void main(String [] args) {
        Spark.get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) {
                return "<h1>Hello World From Spark</h1>";
            }
        });
    }
}
