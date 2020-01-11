package sample.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String _id;
    private String name;
    private List<String> views_id = new ArrayList<>();
    private List<String> views_name = new ArrayList<>();

    public List<String> getViews_name() {
        return views_name;
    }

    public void setViews_name(List<String> views_name) {
        this.views_name = views_name;
    }

    public StringProperty getPropertyName(){
        return new SimpleStringProperty(this.name);
    }

    @Override
    public String toString() {
        return "Group{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", views_id=" + views_id +
                '}';
    }

    public List<String> getViews_id() {
        return views_id;
    }

    public void setViews_id(List<String> views_id) {
        this.views_id = views_id;
    }

    public Group() {
    }

    public Group(String id, String name,List<String> views_id) {
        this._id = id;
        this.name = name;
        this.views_id = views_id;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
