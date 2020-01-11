package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.Database.DBFacade;
import sample.model.Group;
import sample.model.User;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    private DBFacade db = DBFacade.getDb();

    static User user = null;

    private ObservableList<User> users = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //table code
        usernameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User,String>("email"));
        groupColumn.setCellValueFactory(data -> data.getValue().getGroup().getPropertyName());
        List<User> list = db.selectUsers();
        users.addAll(list);
        tableView.setItems(users);

        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                User user1 = tableView.getSelectionModel().getSelectedItem();
                if (user1 != null){
                    nameField.setText(user1.getName());
                    usernameField.setText(user1.getUsername());
                    emailField.setText(user1.getEmail());
                }
            }
        });

        //form code
        groupBox.setCellFactory(param -> new ListCell<Group>(){
            @Override
            protected void updateItem(Group item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        groupBox.setPromptText("select group");
        groupBox.setButtonCell(new ListCell<Group>(){
            @Override
            protected void updateItem(Group item, boolean empty){
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        ObservableList<Group> groups = FXCollections.observableArrayList();
        groups.addAll(db.selectGroups());
        groupBox.setItems(groups);
    }


    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> groupColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableView<User> tableView;

    @FXML
    void backHandler() throws IOException {
        UtilController.changeScene("userPage","Main Page");
    }

    @FXML
    void addUserHandler() {
        if (nameField.getText().equalsIgnoreCase("") || emailField.getText().equalsIgnoreCase("") || passwordField.getText().equalsIgnoreCase("")|| usernameField.getText().equalsIgnoreCase("")){
            AlertsController.warning("Empty Fields !","Please Fill All text Fields");
        }else if (groupBox.getSelectionModel().getSelectedItem() == null){
            AlertsController.warning("Group not Selected !","Please Select a Group For the User");
        }else{
            User user = new User();
            user.setName(nameField.getText());
            user.setUsername(usernameField.getText());
            user.setEmail(emailField.getText());
            user.setPassword(passwordField.getText());
            user.setGroup(groupBox.getSelectionModel().getSelectedItem());
            if (db.addUser(user)){
                users.add(user);
                nameField.setText("");
                emailField.setText("");
                passwordField.setText("");
                usernameField.setText("");
                groupBox.getSelectionModel().clearSelection();
            }else{
                AlertsController.warning("Username is Used !","Please try another Username");
            }
        }
    }

    @FXML
    void editHandler() {
        user = tableView.getSelectionModel().getSelectedItem();
        if (user == null) {
            AlertsController.warning("No User Selected !", "Please Select User from Users Table !");
        }else if(groupBox.getSelectionModel().getSelectedItem() == null){
            AlertsController.warning("Group Not Selected !","Please Select a Group From Groups List\nif you dont want to change The group Select The old one !");
        }else{
            User newUser = new User();
            newUser.setName(nameField.getText());
            newUser.setEmail(emailField.getText());
            newUser.setUsername(usernameField.getText());
            newUser.setPassword(passwordField.getText());
            newUser.setGroup(groupBox.getSelectionModel().getSelectedItem());
            if (!passwordField.getText().equalsIgnoreCase("")){
                if (AlertsController.confirm("Password Field Not Empty !","Do you also want to change the Password !")){
                    long result = db.updateUserWithPassword(user,newUser);
                    if (result > 0){
                        this.clear();
                        AlertsController.info("Updated !","User Updated Successfully !");
                    }else{
                        AlertsController.error("Not Updated !","You have not changed anything in the data fields !");
                    }
                }else{
                    this.updateUserWithoutPassword(user,newUser);
                }
            }else{
                if (AlertsController.confirm("Are You Sure !","Do you want to Update User ("+user.getName()+")"))
                    this.updateUserWithoutPassword(user,newUser);
            }
        }
    }

    private void updateUserWithoutPassword(User user,User newUser){
        long result = db.updateUserWithoutPassword(user,newUser);
        if (result > 0){
            AlertsController.info("Updated !","User Updated Successfully !");
            this.clear();
        }else{
            AlertsController.error("Not Updated !","You have not changed anything in the data fields !");
        }
    }

    @FXML
    void deleteHandler() {
        user = tableView.getSelectionModel().getSelectedItem();
        if (user == null){
            AlertsController.warning("No User Selected !","Please Select User from Users Table !");
        }else{
            if (AlertsController.confirm("Are You Sure !","Do you want to Delete User ("+user.getName()+")")){
                long result = db.deleteUser(user);
                if (result > 0){
                    AlertsController.info("Deleted !","User Deleted Successfully !");
                    this.clear();
                }
            }
        }
    }

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    private ComboBox<Group> groupBox;


    private void clear(){
        nameField.setText("");
        passwordField.setText("");
        usernameField.setText("");
        emailField.setText("");
        groupBox.getSelectionModel().clearSelection();
        users.clear();
        tableView.getItems().clear();
        List<User> list = db.selectUsers();
        users.addAll(list);
        tableView.setItems(users);
    }

    @FXML
    void clearHandler(){
        this.clear();
    }
}
