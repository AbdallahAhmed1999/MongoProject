package sample.controller;

import javafx.beans.binding.IntegerBinding;
import javafx.beans.value.ObservableIntegerValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import sample.Database.DBFacade;
import sample.model.User;
import sample.model.View;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    static User user = null;

    static DBFacade db = DBFacade.getDb();

    @FXML
    private Label groupLabel;

    @FXML
    private VBox buttonsVBox;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label nameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        user = AuthController.user;
        groupLabel.setText(user.getGroup().getName());
        usernameLabel.setText(user.getUsername());
        emailLabel.setText(user.getEmail());
        nameLabel.setText(user.getName());
        List<String> view_ids = user.getGroup().getViews_id();
        List<View> views = new ArrayList<>();
        view_ids.forEach(id -> views.add(db.getView(id)));


        for (View v:views) {
            Button b = new Button(v.getName());
            b.setOnAction(action -> {
                try {
                    UtilController.changeScene(v.getFileName(),v.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            buttonsVBox.getChildren().add(b);
        }
    }


    @FXML
    void logoutHandler() throws IOException {
        UtilController.changeScene("loginPage","Login");
        AuthController.user = null;
        AuthController.patient = null;
    }
}
