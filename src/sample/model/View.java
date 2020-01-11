package sample.model;

import javafx.beans.property.SimpleStringProperty;

public class View {
    private String _id;
    private String name;
    private String fileName;

    @Override
    public String toString() {
        return "View{" +
                "_id='" + _id + '\'' +
                ", name=" + name +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    public View() {
    }

    public View(String id, String name, String fileName) {
        this._id = id;
        this.name = name;
        this.fileName = fileName;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
