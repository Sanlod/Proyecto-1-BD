package com.example.bdbconsultas.Entidades;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Rating {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleIntegerProperty number = new SimpleIntegerProperty();
    private SimpleStringProperty notes = new SimpleStringProperty();
    private SimpleIntegerProperty id_Adoption = new SimpleIntegerProperty();

    public Rating(int id, int number, String notes, int id_Adoption) {
        this.id.set(id);
        this.number.set(number);
        this.notes.set(notes);
        this.id_Adoption.set(id_Adoption);

    }

    public int getId() {
        return id.get();
    }
    public String getNotes() {
        return notes.get();
    }
    public int getNumber() {
        return number.get();
    }
    public int getId_Adoption() {
        return id_Adoption.get();
    }

}
