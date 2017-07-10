package com.mongodb.week1;

import freemarker.template.Configuration;
import freemarker.template.Template;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jsimone on 10/12/15.
 */
public class HelloWorldSparkFreemarkerStyle {
    public static void main(String[] args) {

        final Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(HelloWorldSparkFreemarkerStyle.class, "/");


        Spark.get(new Route("/") {
            @Override
            public Object handle(Request request, Response response) {

                StringWriter writer = new StringWriter();
                try {
                    Template helloTempate = configuration.getTemplate("hello.ftl");

                    Map<String, Object> helloMap = new HashMap<String, Object>();
                    helloMap.put("name", "FreeMarker");

                    helloTempate.process(helloMap, writer);

                } catch (Exception e) {
                    halt(500);
                    e.printStackTrace();
                }

                return writer;
            }
        });
    }
}
