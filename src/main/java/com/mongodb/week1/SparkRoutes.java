package com.mongodb.week1;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Created by jsimone on 10/12/15.
 */
public class SparkRoutes {
    public static void main(String[] args) {
        Spark.get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) {
                return "<h1>Hello World From Spark</h1>";
            }
        });

        // tryhttp://localhost:4567/test
        Spark.get(new Route("/test") {
            @Override
            public Object handle(Request request, Response response) {
                return "this is a test  page";
            }
        });

        // try http://localhost:4567/echo/cat
        // or  http://localhost:4567/echo/dog
        Spark.get(new Route("/echo/:thing") {
            @Override
            public Object handle(Request request, Response response) {
                return request.params(":thing");
            }
        });
    }
}

