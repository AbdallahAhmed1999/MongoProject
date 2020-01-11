package sample;

import com.mongodb.client.FindIterable;
import org.bson.Document;
import sample.Database.*;
import sample.model.Group;
import sample.model.Patient;
import sample.model.User;
import sample.model.View;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class test {
    public static void main(String[] args) {
//        LocalDate date = LocalDate.parse("1999-10-21");
//        LocalDate date1 = LocalDate.now();
//        System.out.println(date1.minusYears(date.getYear()).getYear());

//        FindIterable<Document> all = PatientDB.findAll();
//        for (Document doc:all) {
//            System.out.println(doc.toString());
//        }

//        Group group = new Group();
//        group.setName("test");
//        group.setViews(ViewDB.getViews());
//
//        GroupDB.addGroup(group);



//        Group p = GroupDB.selectGroup("5de9443869b5b64160892863");
//        System.out.println(p.toString());


//        DBFacade dbFacade = DBFacade.getDb();
//        System.out.println(dbFacade.selectGroup("5de9443869b5b64160892863"));

//        User u = UserDB.login("aamra","123456");
//        System.out.println(u);

//        Patient p = PatientDB.login("aamra","123456");
//        System.out.println(p);



//        List<Group> list = GroupDB.selectGroups();
//        list.forEach(group -> System.out.println(group.getId()+group.getName()+group.getViews_id()));

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        System.out.printf("%02d",2);

    }
}
