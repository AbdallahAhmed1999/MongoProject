package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Database.DBFacade;
import sample.model.Group;
import sample.model.View;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GroupController implements Initializable {

    DBFacade db = DBFacade.getDb();

    @FXML
    private TableView<Group> tableView;

    @FXML
    private TableColumn<Group, String> groupName;

    @FXML
    private TableColumn<Group, List<String>> groupViews;

    @FXML
    private TextField groupNameField;

    @FXML
    private ListView<View> listView = new ListView<View>();


    private ObservableList<Group> groupObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // add group code
        ObservableList<View> list = FXCollections.observableArrayList();
        listView.setItems(list);
        List<View> views = db.getViews();
        list.addAll(views);
        listView.setCellFactory(param -> new ListCell<View>() {
            @Override
            protected void updateItem(View item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getName() == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //table code
        groupName.setCellValueFactory(new PropertyValueFactory<Group, String>("name"));
        groupViews.setCellValueFactory(new PropertyValueFactory<Group, List<String>>("views_name"));
        List<Group> selectedGroups = db.selectGroups();
        List<String> views_name1;
        String viewName;
        for (Group group : selectedGroups) {
            views_name1 = new ArrayList<>();
            for (String id : group.getViews_id()) {
                viewName = db.selectViewName(id);
                views_name1.add(viewName);
            }
            group.setViews_name(views_name1);
        }
        groupObservableList.addAll(selectedGroups);
        tableView.setItems(groupObservableList);

    }


    @FXML
    void addGroupHandler() {
        List<View> list = listView.getSelectionModel().getSelectedItems();
        if (groupNameField.getText().equalsIgnoreCase("")) {
            AlertsController.warning("Empty Field !", "Please Fill Group Name : ");
        } else {
            if (list.size() == 0) {
                AlertsController.warning("Views Not Selected!", "Please Select at least one View !");
            } else {
                List<String> ids = new ArrayList<>();
                list.forEach(view -> ids.add(view.getId()));
                Group group = new Group();
                group.setName(groupNameField.getText());
                group.setViews_id(ids);
                db.addGroup(group);
                List<String> views2 = new ArrayList<>();
                for (String id : group.getViews_id()) {
                    String viewName2 = db.selectViewName(id);
                    views2.add(viewName2);
                }
                group.setViews_name(views2);
                groupObservableList.add(group);
            }
        }
    }

    @FXML
    void backHandler() throws IOException {
        UtilController.changeScene("userPage", "Main Page");
    }


    @FXML
    void editHandler() {
        Group group = tableView.getSelectionModel().getSelectedItem();
        ObservableList<View> views = listView.getSelectionModel().getSelectedItems();
        if (group == null || views.size() < 1) {
            AlertsController.error("Group or Views Not Selected !", "Please Select Group from Groups table and Select Views From Views List");
        } else {
            if (AlertsController.confirm("Are You Sure !", "Do you want to Edit " + group.getName() + " Group Views")) {
                List<String> views_id = new ArrayList<>();
                views.forEach(view -> views_id.add(view.getId()));
                long result = db.updateGroupViews(group, views_id);
                if (result > 0) {
                    groupObservableList.clear();
                    tableView.getItems().clear();
                    List<Group> myGroup = db.selectGroups();
                    List<String> views_name;
                    String view_name;
                    for (Group g : myGroup) {
                        views_name = new ArrayList<String>();
                        for (String id : g.getViews_id()) {
                            view_name = db.selectViewName(id);
                            views_name.add(view_name);
                        }
                        g.setViews_name(views_name);
                    }
                    groupObservableList.addAll(myGroup);
                    tableView.setItems(groupObservableList);
                    AlertsController.info("Updated !", "Group Updated Successfully !");
                }
            }
        }
    }

    @FXML
    void deleteHandler() {
        Group group = tableView.getSelectionModel().getSelectedItem();
        if (group == null) {
            AlertsController.error("Group Not Selected !", "Please Select Group from Groups table !");
        } else {
            if (AlertsController.confirm("Are You Sure !", "Do you want to Delete " + group.getName() + " Group !")) {
                if (db.findUsersBelongTo(group) > 0) {
                    AlertsController.error("Invalid Group !","There is Users Belongs to The Group You Want To Delete\nYou Must Change Their Group!");
                } else {
                    long result = db.deleteGroup(group);
                    if (result > 0) {
                        groupObservableList.clear();
                        tableView.getItems().clear();
                        List<Group> myGroup = db.selectGroups();
                        List<String> views_name;
                        String view_name;
                        for (Group g : myGroup) {
                            views_name = new ArrayList<String>();
                            for (String id : g.getViews_id()) {
                                view_name = db.selectViewName(id);
                                views_name.add(view_name);
                            }
                            g.setViews_name(views_name);
                        }
                        groupObservableList.addAll(myGroup);
                        tableView.setItems(groupObservableList);
                        AlertsController.info("Deleted !", "Group Deleted Successfully !");
                    }
                }
            }
        }
    }
}
