package sample.Database;

import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import sample.model.View;

import java.util.ArrayList;
import java.util.List;

public class ViewDB {

    private static DBConnection connection = DBConnection.getConnection();
    private static MongoDatabase db = connection.getDb();
    private static MongoCollection<Document> collection = db.getCollection("views");

    List<View> getViews() {
        List<View> views = new ArrayList<>();
        for (Document doc : collection.find()) {
            ObjectId id = (ObjectId) doc.get("_id");
            views.add(
                    new View(
                            id.toString(),
                            (String)doc.get("name"),
                            (String)doc.get("file_name")
                    )
            );
        }
        return views;
    }

    String getViewName(String id){
        ObjectId id1 = new ObjectId(id);
        Document doc = collection.find(Filters.eq("_id",id1)).first();
        return doc.get("name").toString();
    }

    View getView(String id){
        ObjectId id1 = new ObjectId(id);
        Document doc = collection.find(Filters.eq("_id",id1)).first();
        return new View(id,doc.get("name").toString(),doc.get("file_name").toString());
    }

}
