package sample.Database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.types.ObjectId;
import sample.controller.UtilController;
import sample.model.Patient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class PatientDB {


    private static DBConnection connection = DBConnection.getConnection();
    private static MongoDatabase db = connection.getDb();
    private static MongoCollection<Document> collection = db.getCollection("patients");

    Object[] register(Patient patient) {
        long count = collection.count(Filters.eq("username", patient.getUsername()));
        if (count > 0) {
            return new Object[]{false, "The Username is Used Pleas try another one !"};
        } else {
            String password = UtilController.md5(patient.getPassword());
            collection.insertOne(
                    new Document("name", patient.getName())
                            .append("username", patient.getUsername())
                            .append("phone", patient.getPhone())
                            .append("DOB", patient.getDOB().toString())
                            .append("password", password)
                            .append("email",patient.getEmail())
            );
            return new Object[]{true, "Patient " + patient.getName() + " Registered Successfully !"};
        }
    }

    Patient login(String username, String pass) {
        String password = UtilController.md5(pass);
        Document doc = collection.find(Filters.and(Filters.eq("username", username), Filters.eq("password", password))).first();
        try {
            ObjectId id = (ObjectId) doc.get("_id");
            String name = (String) doc.get("name");
            String username1 = (String) doc.get("username");
            String phone = (String) doc.get("phone");
            LocalDate date = LocalDate.parse((String) doc.get("DOB"));
            LocalDate now = LocalDate.now();
            int age = now.minusYears(date.getYear()).getYear();
            String email = (String) doc.get("email");
            return new Patient(id.toString(), name, username1, email, phone, date, age);
        } catch (NullPointerException n) {
            return null;
        }
    }

    List<Patient> findAll() {
        List<Patient> list = new ArrayList<>();
        LocalDate dob = null;
        LocalDate now = LocalDate.now();
        long age = 0;
        for (Document doc : collection.find()) {
            dob = LocalDate.parse(doc.get("DOB").toString());
            age = now.minusYears(dob.getYear()).getYear();
            Long agelong = new Long(age);
            list.add(
                    new Patient(
                            doc.get("_id").toString(),
                            doc.get("name").toString(),
                            doc.get("username").toString(),
                            doc.get("email").toString(),
                            doc.get("phone").toString(),
                            dob,
                            agelong.intValue()
                    )
            );
        }
        return list;
    }

    List<Patient> searchPatients(String value) {
        List<Patient> patients = new ArrayList<>();
        LocalDate dob = null;
        LocalDate now = LocalDate.now();
        long age = 0;
        for (Document doc : collection.find(
                Filters.or(Filters.eq("name", value)
                        , Filters.eq("phone", value)
                        , Filters.eq("username", value)
                )
            )
        ) {
            dob = LocalDate.parse(doc.get("DOB").toString());
            age = now.minusYears(dob.getYear()).getYear();
            Long agelong = new Long(age);
            patients.add(
                    new Patient(
                            doc.get("_id").toString(),
                            doc.get("name").toString(),
                            doc.get("username").toString(),
                            doc.get("email").toString(),
                            doc.get("phone").toString(),
                            dob,
                            agelong.intValue()
                    )
            );
        }
        return patients;
    }

    Patient findPatient(String id){
        ObjectId id1 = new ObjectId(id);
        Document doc = collection.find(Filters.eq("_id",id1)).first();
        return new Patient(id,doc.get("name").toString(),doc.getString("username"),doc.getString("email"),doc.getString("phone"));
    }
}
