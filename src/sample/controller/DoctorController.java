package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Database.DBFacade;
import sample.model.Patient;
import sample.model.Visit;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DoctorController implements Initializable {

    private DBFacade db = DBFacade.getDb();

    Patient patient = null;

    public static Visit visit = null;

    ObservableList<Patient> patientObservableList = FXCollections.observableArrayList();
    ObservableList<Visit> visitObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        patientPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        patientAgeColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        patientUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        List<Patient> patients = db.selectPatients();
        patientObservableList.addAll(patients);
        tableView1.setItems(patientObservableList);

        visitNoteColumn.setCellValueFactory(new PropertyValueFactory<>("notes"));
        visitTimeColumn.setCellValueFactory(data -> data.getValue().getDateProperty());
    }

    // visits table
    @FXML
    private TableView<Visit> tableView2;

    @FXML
    private TableColumn<Visit, String> visitNoteColumn;

    @FXML
    private TableColumn<Visit, String> visitTimeColumn;


    //patient table
    @FXML
    private TableView<Patient> tableView1;

    @FXML
    private TableColumn<Patient, Long> patientAgeColumn;

    @FXML
    private TableColumn<Patient, String> patientPhoneColumn;

    @FXML
    private TableColumn<Patient, String> patientUsernameColumn;

    @FXML
    private TableColumn<Patient, String> patientNameColumn;

    @FXML
    private TextField searchField;

    @FXML
    void searchHandler() {
        patientObservableList.clear();
        tableView1.getItems().clear();
        tableView2.getItems().clear();
        if(searchField.getText().equalsIgnoreCase("")){
            List<Patient> patients = db.selectPatients();
            patientObservableList.addAll(patients);
            tableView1.setItems(patientObservableList);
        }else{
            List<Patient> patients = db.searchPatients(searchField.getText());
            patientObservableList.addAll(patients);
            tableView1.setItems(patientObservableList);
        }
    }

    @FXML
    void selectedPatientHandler(){
        patient =  tableView1.getSelectionModel().getSelectedItem();
        System.out.println(patient);
        if (patient != null){
            tableView2.getItems().clear();
            List<Visit> visits = db.getVisits(patient);
            if (visits.size() == 0){
                AlertsController.info("No Visits !","This Patient has No Visits !");
            }
            visitObservableList.addAll(visits);
            tableView2.setItems(visitObservableList);
            patient = null;
        }
    }

    @FXML
    void backHandler() throws IOException {
        UtilController.changeScene("userPage","Main Page");
    }

    @FXML
    void editHandler() throws IOException {
        visit = tableView2.getSelectionModel().getSelectedItem();
        if (visit == null){
            AlertsController.error("Visit Not Selected !","Please Select a Visit From Visits Table !");
        }else{
            UtilController.changeScene("inputDialog","Update Notes");
        }
    }


}
