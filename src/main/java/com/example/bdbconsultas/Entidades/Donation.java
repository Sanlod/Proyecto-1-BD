package com.example.bdbconsultas.Entidades;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class Donation {
    private SimpleIntegerProperty ammount = new SimpleIntegerProperty();
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleIntegerProperty id_person = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> fecha = new SimpleObjectProperty<>();

    public Donation(int ammount, int id, int id_person, LocalDate fecha) {
        this.ammount.set(ammount);
        this.id.set(id);
        this.id_person.set(id_person);
        this.fecha.set(fecha);
    }

    public int getAmmount() {return ammount.get();}
    public int getId() {return id.get();}
    public int getId_person() {return id_person.get();}
    public LocalDate getFecha() {return fecha.get();}
}
