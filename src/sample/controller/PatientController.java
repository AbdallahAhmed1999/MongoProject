package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Database.DBFacade;
import sample.model.Patient;
import sample.model.Reservation;
import sample.model.Visit;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PatientController implements Initializable {

    ObservableList<Visit> visitObservableList = FXCollections.observableArrayList();
    DBFacade db = DBFacade.getDb();
    Patient patient = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        patient = AuthController.patient;
        patientLabel.setText(patient.getName()+" Visits");
        notesColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        visitObservableList.addAll(db.getVisits(patient));
        tableView.setItems(visitObservableList);
    }

    @FXML
    private TableColumn<Visit, String> notesColumn;

    @FXML
    private TableColumn<Visit, String> timeColumn;

    @FXML
    private Label patientLabel;

    @FXML
    private TableView<Visit> tableView;

    @FXML
    void requestHandler() {
        List<String> appointments = new ArrayList<>();
        appointments.add("Special");
        appointments.add("Regular");

        ChoiceDialog<String> dialog = new ChoiceDialog<>(appointments.get(1),appointments);
        dialog.setTitle("Reservation");
        dialog.setHeaderText("Choose Your Appointment Type .");
        dialog.setContentText(null);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Reservation reservation = new Reservation(result.get());
            LocalDateTime time = reservation.reserve();
            Visit visit = new Visit();
            visit.setDate(time);
            visit.setNotes("");
            visit.setPatient(this.patient);
            db.insertVisit(visit);
            visitObservableList.clear();
            tableView.getItems().clear();
            visitObservableList.addAll(db.getVisits(patient));
            tableView.setItems(visitObservableList);
        }
    }

    @FXML
    void logoutHandler() throws IOException {
        UtilController.changeScene("loginPage","Login");
        AuthController.patient = null;
        AuthController.user = null;
    }

}
