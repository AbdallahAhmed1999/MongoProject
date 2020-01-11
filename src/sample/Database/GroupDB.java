package sample.Database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.types.ObjectId;
import sample.model.Group;


import java.util.ArrayList;
import java.util.List;


public class GroupDB {
    private static DBConnection connection = DBConnection.getConnection();
    private static MongoDatabase db = connection.getDb();
    private static MongoCollection<Document> collection = db.getCollection("groups");

    void addGroup(Group group) {
        collection.insertOne(
                new Document("name", group.getName())
                        .append("views", group.getViews_id())
        );
    }

    Group selectGroup(String id) {
        ObjectId id2 = new ObjectId(id);
        Document doc = collection.find(Filters.eq("_id", id2)).first();

        String idstring = doc.get("_id").toString();
        String name = (String) doc.get("name");
        ArrayList<String> list = (ArrayList<String>) doc.get("views");

        return new Group(idstring, name, list);
    }

    List<Group> selectGroups() {
        List<Group> groups = new ArrayList<>();
        for (Document doc : collection.find()) {
            ObjectId id = (ObjectId) doc.get("_id");
            groups.add(
                    new Group(
                            id.toString(), (String) doc.get("name"), (List<String>) doc.get("views")
                    )
            );
        }
        return groups;
    }

    long updateGroupViews(Group group,List<String> views_id){
        Document doc = new Document("$set",new Document("views",views_id));
        ObjectId id = new ObjectId(group.getId());
        UpdateResult result = collection.updateOne(Filters.eq("_id",id),doc);
        return result.getModifiedCount();
    }

    long deleteGroup(Group group){
        ObjectId id = new ObjectId(group.getId());
        DeleteResult result = collection.deleteOne(Filters.eq("_id",id));
        return result.getDeletedCount();
    }
}
