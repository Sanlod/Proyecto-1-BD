package com.example.bdbconsultas.Entidades;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Association {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty nombre = new SimpleStringProperty();

    public Association(int id, String nombre) {
        this.id.set(id);
        this.nombre.set(nombre);
    }

    public int getId() {return id.get();}
    public String getNombre() {return nombre.get();}
}
