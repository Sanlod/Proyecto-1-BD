package com.example.bdbconsultas.Entidades;

import javafx.beans.property.SimpleIntegerProperty;

public class BlackList {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();

    BlackList(int id){
        this.id.set(id);
    }

    public int getId(){return id.get();}
}
