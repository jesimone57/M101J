package com.mongodb.finalexam;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by jsimone on 12/6/15.
 */
public class Question7 {
    public static void main(String[] args) {
        MongoClient client = new MongoClient("localhost");
        MongoDatabase db = client.getDatabase("final7");
        MongoCollection<Document> images = db.getCollection("images");
        MongoCollection<Document> albums = db.getCollection("albums");

        // individual deletion
        //coll.deleteOne(eq("_id", 5));
        int count = 0;

        List<Document> allImages = images.find().into(new ArrayList<Document>());
        for (Document curImage:  allImages) {
            Integer imageId = curImage.getInteger("_id");
            System.out.println(imageId);
            Bson filter =  eq("images", imageId);
            MongoCursor<Document> results = albums.find(filter).iterator();
            if ( ! results.hasNext()) {
                    count++;
                   images.deleteOne(eq("_id", imageId));
                 }
        }

//        List<Document> allAlbums = albums.find().into(new ArrayList<Document>());
//        for (Document curAlbum:  allAlbums) {
//            System.out.println(curAlbum.toJson());
//            List<Integer> imageIds = (List<Integer>)curAlbum.get("images");
//            for (Integer imageId: imageIds) {
//                 System.out.println("  imageId ="+imageId);
//                Bson filter =  eq("_id", imageId);
//               MongoCursor<Document> results = images.find(filter).iterator();
//                if ( results.hasNext()) {
//                    count++;
//                }
//                System.out.println("results size "+results.size());
//                if ( results.isEmpty() ) {
//                    count++;
//                    System.out.println("  null imageDoc "+imageId);
//                }
//            }
//        }

        System.out.println("count = "+count);
    }
}
