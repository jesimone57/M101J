package course;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;

public class BlogPostDAO {
    MongoCollection<Document> postsCollection;

    public BlogPostDAO(final MongoDatabase blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    // Return a single post corresponding to a permalink
    public Document findByPermalink(String permalink) {
        Document post = postsCollection.find(eq("permalink", permalink)).first();

        // fix up if a post has no likes
        if (post != null) {
            List<Document> comments = (List<Document>) post.get("comments");
            for (Document comment : comments) {
                if (!comment.containsKey("num_likes")) {
                    comment.put("num_likes", 0);
                }
            }
        }
        return post;

    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
    public List<Document> findByDateDescending(int limit) {
        return postsCollection.find().sort(descending("date"))
                .limit(limit)
                .into(new ArrayList<Document>());
    }

    public List<Document> findByTagDateDescending(final String tag) {
        return postsCollection.find(eq("tags", tag))
                .sort(descending("date"))
                .limit(10)
                .into(new ArrayList<Document>());
    }


    public String addPost(String title, String body, List tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", "");     // get rid of non alphanumeric
        permalink = permalink.toLowerCase();             // lowercase it
        permalink = permalink+ (new Date()).getTime();   // append timestamp to make it unique


        // todo XXX
        // Remember that a valid post has the following keys:
        // author, body, permalink, tags, comments, date
        //
        // A few hints:
        // - Don't forget to create an empty list of comments
        // - for the value of the date key, today's datetime is fine.
        // - tags are already in list form that implements suitable interface.
        // - we created the permalink for you above.

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm:ss a");
        String now = sdf.format(new Date().getTime());

        // Build the post object and insert it
        Document post = new Document("author", username)
                .append("title", title)
                .append("body", body)
                .append("permalink", permalink)
                .append("tags", tags)
                .append("comments", new ArrayList<String>())
                .append("date", new Date(System.currentTimeMillis()));

        postsCollection.insertOne(post);

        return permalink;
    }


    // Append a comment to a blog post
    public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {

        Bson filter = eq("permalink", permalink);
        Document commentDoc = new Document("body", body);
        if (StringUtils.isNotBlank(name)) {
            commentDoc.append("author", name);
        }
        if (StringUtils.isNotBlank(email)) {
            commentDoc.append("email", email);
        }
        //System.out.println("====>"+commentDoc.toJson());
        postsCollection.updateOne(filter, new Document("$push", new Document("comments", commentDoc)));

        // todo  XXX
        // Hints:
        // - email is optional and may come in NULL. Check for that.
        // - best solution uses an update command to the database and a suitable
        //   operator to append the comment on to any existing list of comments

    }

    public void likePost(final String permalink, final int ordinal) {

        // This is what it would look like in the shell
        //db.posts.update( {"permalink" : "test1449518999945"}, {$inc: {"comments.0.num_likes": 1} })

        postsCollection.updateOne(eq("permalink", permalink),
                new Document("$inc", new Document("comments."+ordinal+".num_likes", 1)));

//        Document post = postsCollection.find(eq("permalink", permalink)).first();
//        if (post != null) {
//            List<Document> comments = (List<Document>) post.get("comments");
//            Document comment = comments.get(ordinal);
//
//            post.append("$inc", new Document("comments."+ordinal+".num_likes", 1));

//            if (! comment.containsKey("num_likes")) {
//                comment.put("num_likes", 1);
//            } else {
//                int nlikes = (Integer)comment.get("num_likes");
//                comment.put("num_likes", nlikes+1);
//            }

           // System.out.println("---------->" + comment.toJson());

       // }

        // XXX Final Question 4 - work here
        // You must increment the number of likes on the comment in position `ordinal`
        // on the post identified by `permalink`.
        //
        //
    }
}
