package com.example.bdbconsultas.Entidades;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

public class Adoption {
    private SimpleIntegerProperty id =  new SimpleIntegerProperty();
    private SimpleStringProperty notes = new SimpleStringProperty();
    private ObjectProperty<Image> photo = new SimpleObjectProperty<>();
    private SimpleStringProperty id_Person = new SimpleStringProperty();

    public Adoption(int id, String notes, Image photo, String id_Person){
        this.id = new SimpleIntegerProperty(id);
        this.notes = new SimpleStringProperty(notes);
        this.photo = new SimpleObjectProperty(photo);
        this.id_Person = new SimpleStringProperty(id_Person);

    }

    public int getId(){return id.get();}
    public String getNotes(){return notes.get();}
    public Image getPhoto(){return photo.get();}
    public String getId_Person(){return id_Person.get();}


}
