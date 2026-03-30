package com.example.bdbconsultas.Entidades;

import javafx.beans.property.SimpleIntegerProperty;

public class Rescuer {
    private SimpleIntegerProperty id_Person = new SimpleIntegerProperty();

    public Rescuer(int id){
        this.id_Person.set(id);
    }
    public int getId(){return id_Person.get();}
}
