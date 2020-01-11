package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.Database.DBFacade;
import sample.model.Patient;
import sample.model.User;


import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AuthController implements Initializable {

    static DBFacade db = DBFacade.getDb();
    public static User user = null;
    public static Patient patient = null;

    @FXML
    private CheckBox isAdmin;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void CancelHandler() {
        System.exit(0);
    }

    @FXML
    void LoginHandler() throws IOException {
        if (usernameField.getText().equalsIgnoreCase("") || passwordField.getText().equalsIgnoreCase("")) {
            AlertsController.warning("Empty Fields !", "Please Fill All Fields");
        } else if (isAdmin.isSelected()) {
            user = db.userLogin(usernameField.getText(),passwordField.getText());
            if (user == null){
                AlertsController.error("Not Found !", "Sorry Your data does't match our records");
            }else {
                UtilController.changeScene("userPage", "Main Page");
                System.out.println(user);
            }
        }else if((patient = db.patientLogin(usernameField.getText(),passwordField.getText())) != null){
            UtilController.changeScene("patientPage", "Main Page");
        } else {
            AlertsController.error("Not Found !", "Sorry Your data does't match our records");
        }
    }

    @FXML
    void registerHandler() throws IOException {
        UtilController.changeScene("registerPage", "Register Page");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    @FXML
    private TextField registerPhone;

    @FXML
    private PasswordField registerPassword;

    @FXML
    private TextField registerEmail;

    @FXML
    private DatePicker registerDOB;

    @FXML
    private PasswordField registerPasswordConfirmation;

    @FXML
    private TextField registerName;

    @FXML
    private TextField registerUsername;

    @FXML
    void SignUpHandler() throws IOException {
        if (registerDOB.getEditor().getText().equalsIgnoreCase("") || registerName.getText().equalsIgnoreCase("") || registerUsername.getText().equalsIgnoreCase("") || registerEmail.getText().equalsIgnoreCase("") || registerPassword.getText().equalsIgnoreCase("") || !registerPassword.getText().equalsIgnoreCase(registerPasswordConfirmation.getText())) {
            AlertsController.warning("Empty Fields !", "Please Fill required Fields or Check if the password is Confirmed !");
        } else {
            LocalDate dob = registerDOB.getValue();
            Patient patient = new Patient(
                    registerName.getText(),
                    registerUsername.getText(),
                    registerEmail.getText(),
                    registerPassword.getText(),
                    registerPhone.getText(),
                    dob
            );
            Object[] registered = db.registerPatient(patient);
            if ((boolean) registered[0]) {
                AuthController.patient = patient;
                UtilController.changeScene("patientPage","Main View");
                AlertsController.success("success", registered[1].toString());
            } else {
                AlertsController.error("Failed", registered[1].toString());
            }
        }
    }

    @FXML
    void CancelRegisterHandler() throws IOException {
        UtilController.changeScene("loginPage", "login");
    }
}
