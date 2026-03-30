package com.example.bdbconsultas.Entidades;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Race {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleIntegerProperty id_animal = new SimpleIntegerProperty();

    public Race(int id, String name, int id_animal){
        this.id.set(id);
        this.name.set(name);
        this.id_animal.set(id_animal);
    }

    public int getId(){
        return id.get();
    }
    public String getName(){
        return name.get();
    }
    public int getId_animal(){
        return id_animal.get();
    }

}
