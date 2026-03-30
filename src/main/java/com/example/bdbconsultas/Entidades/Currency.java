package com.example.bdbconsultas.Entidades;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Currency {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty name = new SimpleStringProperty();

    public Currency(int id, int amount) {
        this.id.set(id);
        this.name.set(String.valueOf(amount));
    }

    public int getId() {
        return id.get();
    }
    public String getName(){
        return name.get();
    }
}
