package com.mongodb.week1;

import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jsimone on 10/12/15.
 */
public class SparkFormHandling {
    public static void main(String[] args) {

        // configure freemarker templating ...
        final Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(SparkFormHandling.class, "/");

        // configure routes ...
        Spark.get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) {

                try {
                    Map<String, Object> fruitsMap = new HashMap<String, Object>();
                    fruitsMap.put("fruits", Arrays.asList("apple", "orange", "banana", "peach"));

                    Template fruitPickerTempate = configuration.getTemplate("fruitPicker.ftl");
                    StringWriter writer = new StringWriter();

                    fruitPickerTempate.process(fruitsMap, writer);
                    return writer;

                } catch (Exception e) {
                    halt(500);
                    return null;
                }
            }
        });

        Spark.post(new Route("/favorite_fruit") {
            @Override
            public Object handle(Request request, Response response) {

                final String fruit = request.queryParams("fruit");
                if (fruit == null) {
                    return "why don't you pick one?";
                } else {
                    return "Your favorite fruit is " + fruit;
                }

            }
        });
    }
}
