package sample.Database;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import sample.controller.UtilController;
import sample.model.Group;
import sample.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDB {

    private static DBConnection connection = DBConnection.getConnection();
    private static MongoDatabase db = connection.getDb();
    private static MongoCollection<Document> collection = db.getCollection("users");
    private static DBFacade dbFacade = DBFacade.getDb();

    boolean addUser(User user) {
        Document doc = collection.find(Filters.eq("username", user.getUsername())).first();
        if (doc == null) {
            String password = UtilController.md5(user.getPassword());
            collection.insertOne(
                    new Document("name", user.getName())
                            .append("email", user.getEmail())
                            .append("username", user.getUsername())
                            .append("password", password)
                            .append("group", user.getGroup().getId())
            );
            return true;
        } else {
            return false;
        }
    }


    List<User> selectUsers() {
        List<User> list = new ArrayList<>();
        for (Document doc : collection.find()) {
            ObjectId objectId = (ObjectId) doc.get("_id");
            list.add(
                    new User(
                            objectId.toString(),
                            doc.get("name").toString(),
                            doc.get("username").toString(),
                            doc.getString("email").toString(),
                            doc.get("password").toString(),
                            dbFacade.selectGroup((String) doc.get("group"))
                    )
            );
        }
        return list;
    }

    User login(String username, String password) {
        try {
            String pass = UtilController.md5(password);
            FindIterable<Document> docs = collection.find(Filters.and(Filters.eq("username", username), Filters.eq("password", pass)));
            Document doc = docs.first();
            ObjectId id = (ObjectId) doc.get("_id");
            if (id == null) {
                return null;
            }
            Group p = dbFacade.selectGroup((String) doc.get("group"));

            User user = new User();
            user.setId(id.toString());
            user.setName((String) doc.get("name"));
            user.setEmail((String) doc.get("email"));
            user.setUsername((String) doc.get("username"));
            user.setGroup(p);

            return user;
        } catch (NullPointerException n) {
            return null;
        }
    }


    int findUsersBelongsTo(Group g){
        FindIterable<Document> docs = collection.find(Filters.eq("group",g.getId()));
        AtomicInteger counter = new AtomicInteger();
        docs.forEach((Block<? super Document>) document -> counter.getAndIncrement());
        return counter.get();
    }


    long updateUserWithPassword(User user,User newUser){
        String password = UtilController.md5(newUser.getPassword());
        Document doc = new Document(
                "$set",
                new Document("name",newUser.getName())
                .append("username",newUser.getUsername())
                .append("email",newUser.getEmail())
                .append("password",password)
                .append("group",newUser.getGroup().getId())
        );
        ObjectId id = new ObjectId(user.getId());
        UpdateResult result = collection.updateOne(Filters.eq("_id",id),doc);
        return result.getModifiedCount();
    }

    long updateUserWithoutPassword(User user,User newUser){
        Document doc = new Document(
                "$set",
                new Document("name",newUser.getName())
                        .append("username",newUser.getUsername())
                        .append("email",newUser.getEmail())
                        .append("group",newUser.getGroup().getId())
        );
        ObjectId id = new ObjectId(user.getId());
        UpdateResult result = collection.updateOne(Filters.eq("_id",id),doc);
        return result.getModifiedCount();
    }

    long deleteUser(User user){
        ObjectId id = new ObjectId(user.getId());
        DeleteResult result = collection.deleteOne(Filters.eq("_id",id));
        return result.getDeletedCount();
    }
}
