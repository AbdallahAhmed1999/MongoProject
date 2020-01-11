package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import sample.Database.DBFacade;
import sample.model.Visit;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DialogController implements Initializable {

    private DBFacade db = DBFacade.getDb();

    private Visit visit = DoctorController.visit;

    @FXML
    private TextArea notesTextArea;

    @FXML
    void updateHandler() throws IOException {
        if (AlertsController.confirm("Are You Sure !","Do You Want To update "+visit.getPatient().getName()+"\nVisit notes "+visit.getDate().toString()+" !")){
            String newNotes = notesTextArea.getText();
            long result = db.updateVisitNotes(visit,newNotes);
            if (result > 0){
                UtilController.changeScene("allPatients","Patients");
                AlertsController.info("Updated !","Notes Updated Successfully !");
            }
        }
    }

    @FXML
    void cancelHandler() throws IOException {
        UtilController.changeScene("allPatients","Patients");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notesTextArea.setText(visit.getNotes());
    }
}
