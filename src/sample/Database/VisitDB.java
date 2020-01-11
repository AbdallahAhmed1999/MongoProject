package sample.Database;

import com.mongodb.BasicDBObject;
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import org.bson.types.ObjectId;
import sample.model.Patient;
import sample.model.Visit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VisitDB {

    private static DBConnection connection = DBConnection.getConnection();
    private static MongoDatabase db = connection.getDb();
    private static MongoCollection<Document> collection = db.getCollection("visits");
    private static DBFacade dbFacade = DBFacade.getDb();

    void insertVisit(Visit visit){
        collection.insertOne(
                new Document("notes",visit.getNotes())
                .append("time",visit.getDate().toString())
                .append("patient_id",visit.getPatient().getId())
        );
    }

    List<Visit> getVisits(Patient patient){
        List<Visit> visits = new ArrayList<>();
        LocalDateTime dateTime = null;
        for (Document doc:collection.find(Filters.eq("patient_id",patient.getId()))) {
            dateTime = LocalDateTime.parse(doc.get("time").toString());
            visits.add(
                    new Visit(
                        doc.get("_id").toString(),
                        doc.getString("notes"),
                        dateTime,
                        patient
                    )
            );
        }
        return visits;
    }

    List<Visit> allVisits(){
        List<Visit> visits = new ArrayList<>();
        LocalDateTime dateTime = null;
        for (Document doc:collection.find().sort(new BasicDBObject("time",-1))) {
            dateTime = LocalDateTime.parse(doc.get("time").toString());
            visits.add(
                    new Visit(
                            doc.get("_id").toString(),
                            doc.get("notes").toString(),
                            dateTime,
                            dbFacade.findPatient(doc.get("patient_id").toString())
                    )
            );
        }
        return visits;
    }

    long updateVisitDate(Visit visit,String newDate){
        Document doc = new Document("$set",new Document("time",newDate));
        ObjectId id = new ObjectId(visit.getId());
        UpdateResult result = collection.updateOne(Filters.eq("_id",id),doc);
        return result.getModifiedCount();
    }

    long deleteVisit(Visit visit){
        ObjectId id = new ObjectId(visit.getId());
        DeleteResult result = collection.deleteOne(Filters.eq("_id",id));
        return result.getDeletedCount();
    }

    long updateVisitNotes(Visit visit,String newNotes){
        Document doc = new Document("$set",new Document("notes",newNotes));
        ObjectId id = new ObjectId(visit.getId());
        UpdateResult result = collection.updateOne(Filters.eq("_id",id),doc);
        return result.getModifiedCount();
    }

}
