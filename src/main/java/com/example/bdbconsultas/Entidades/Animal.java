package com.example.bdbconsultas.Entidades;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Animal {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty name = new SimpleStringProperty();

    public Animal(int id, String name){
        this.id.set(id);
        this.name.set(name);
    }

    public int getId(){
        return id.get();
    }
    public String getName(){
        return name.get();
    }

}
