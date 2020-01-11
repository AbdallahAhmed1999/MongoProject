package sample.model;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Visit {
    private String id;
    private String notes;
    private LocalDateTime date;
    private Patient patient;

    public Visit() {
    }

    public SimpleStringProperty getDateProperty(){
        return new SimpleStringProperty(date.toString());
    }

    public SimpleStringProperty getNotesProperty(){
        return new SimpleStringProperty(notes);
    }

    public Visit(String id, String notes, LocalDateTime date, Patient patient) {
        this.id = id;
        this.notes = notes;
        this.date = date;
        this.patient = patient;
    }

    public Visit(String notes, LocalDateTime date, Patient patient) {
        this.notes = notes;
        this.date = date;
        this.patient = patient;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
