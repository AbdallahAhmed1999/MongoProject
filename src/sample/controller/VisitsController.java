package sample.controller;

import com.jfoenix.controls.JFXSlider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sample.Database.DBFacade;
import sample.model.Patient;
import sample.model.Visit;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class VisitsController implements Initializable {

    private DBFacade db = DBFacade.getDb();

    private ObservableList<Visit> visitObservableList = FXCollections.observableArrayList();

    private ObservableList<Patient> patientObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // table code
        dateColumn.setCellValueFactory(data -> data.getValue().getDateProperty());
        notesColumn.setCellValueFactory(data -> data.getValue().getNotesProperty());
        nameColumn.setCellValueFactory(data -> data.getValue().getPatient().getNameProperty());
        phoneColumn.setCellValueFactory(data -> data.getValue().getPatient().getPhoneProperty());
        visitObservableList.addAll(db.allVisits());
        tableView.setItems(visitObservableList);


        // list view code
        patientListView.setCellFactory(param -> new ListCell<Patient>() {
            @Override
            protected void updateItem(Patient item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(String.format("| %-25s| %-10s| %-10s|", item.getName(), item.getPhone(), item.getUsername()));
                }
            }
        });
        patientObservableList.addAll(db.selectPatients());
        patientListView.setItems(patientObservableList);

    }

    @FXML
    private TableView<Visit> tableView;

    @FXML
    private TableColumn<Visit, String> notesColumn;

    @FXML
    private TableColumn<Visit, String> nameColumn;

    @FXML
    private TableColumn<Visit, String> dateColumn;

    @FXML
    private TableColumn<Visit, String> phoneColumn;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Patient> patientListView;

    @FXML
    private JFXSlider visitMinute;

    @FXML
    private JFXSlider visitHour;

    @FXML
    private DatePicker visitDate;


    @FXML
    void searchHandler() {
        patientObservableList.clear();
        patientListView.getItems().clear();
        if (searchField.getText().equalsIgnoreCase("")) {
            patientObservableList.addAll(db.selectPatients());
            patientListView.setItems(patientObservableList);
        } else {
            patientObservableList.addAll(db.searchPatients(searchField.getText()));
            patientListView.setItems(patientObservableList);
        }
    }

    @FXML
    void addVisitHandler() {
        if (visitDate.getEditor().getText().equalsIgnoreCase("") || patientListView.getSelectionModel().getSelectedItem() == null) {
            AlertsController.error("Empty Fields !", "Please pick a Date and Select a Patient !");
        } else {
            LocalDate date = visitDate.getValue();
            int hour = (int) visitHour.getValue();
            int minute = (int) visitMinute.getValue();
            String dateTime = String.format("%sT%02d:%02d:00.000", date.toString(), hour, minute);
            LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
            Patient patient = patientListView.getSelectionModel().getSelectedItem();
            Visit visit = new Visit("", localDateTime, patient);
            db.insertVisit(visit);
            visitObservableList.add(visit);
        }
    }

    @FXML
    void backHandler() throws IOException {
        UtilController.changeScene("userPage", "Main Page");
    }

    @FXML
    void editVisitHandler() {
        if (tableView.getSelectionModel().getSelectedItem() == null || visitDate.getEditor().getText().equalsIgnoreCase("")){
            AlertsController.error("Visit or Date not Selected !","Please Select a Visit From Visits Table and Select a Date !");
        }else{
            Visit visit = tableView.getSelectionModel().getSelectedItem();
            if (AlertsController.confirm("Are You Sure !","Do You Want To Edit "+visit.getPatient().getName()+" Visit !")){
                LocalDate date = visitDate.getValue();
                int hour = (int) visitHour.getValue();
                int minute = (int) visitMinute.getValue();
                String dateTime = String.format("%sT%02d:%02d:00.000", date.toString(), hour, minute);
                LocalDateTime localDateTime = LocalDateTime.parse(dateTime);
                long result = db.updateVisitDate(visit,localDateTime.toString());
                if (result > 0) {
                    AlertsController.info("Edited !",visit.getPatient().getName()+" Visit Edited Successfully !");
                    visitObservableList.clear();
                    tableView.getItems().clear();
                    visitObservableList.addAll(db.allVisits());
                    tableView.setItems(visitObservableList);
                    visitDate.getEditor().clear();
                }else{
                    AlertsController.error("Not Edited !","Some Thing went wrong !");
                }
            }
        }
    }

    @FXML
    void deleteVisitHandler() {
        if(tableView.getSelectionModel().getSelectedItem() == null){
            AlertsController.error("Visit not Selected !","Please Select a Visit From Visits Table !");
        }else{
            Visit visit = tableView.getSelectionModel().getSelectedItem();
            if (AlertsController.confirm("Are You Sure !","Do You Want To Delete "+visit.getPatient().getName()+" Visit !")){
                long result = db.deleteVisit(visit);
                if (result > 0) {
                    AlertsController.info("Deleted !",visit.getPatient().getName()+" Visit Deleted Successfully !");
                    visitObservableList.clear();
                    tableView.getItems().clear();
                    visitObservableList.addAll(db.allVisits());
                    tableView.setItems(visitObservableList);
                    visitDate.getEditor().clear();
                }else{
                    AlertsController.error("Not Edited !","Some Thing went wrong !");
                }
            }
        }
    }

}
