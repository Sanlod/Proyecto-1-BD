package com.example.bdbconsultas.Entidades;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CribHouse {
    private SimpleIntegerProperty id_Person = new SimpleIntegerProperty();
    private SimpleStringProperty requires_food_donations = new SimpleStringProperty();
    private SimpleStringProperty accepted_pet_type = new SimpleStringProperty();
    private SimpleStringProperty accepted_pet_size = new SimpleStringProperty();
    private SimpleStringProperty location = new SimpleStringProperty();

    public CribHouse(int id_Person, String requires_food_donations, String accepted_pet_type, String accepted_pet_size, String location){
        this.id_Person.set(id_Person);
        this.requires_food_donations.set(requires_food_donations);
        this.accepted_pet_type.set(accepted_pet_type);
        this.accepted_pet_size.set(accepted_pet_size);
        this.location.set(location);

    }
    public int getId_Person(){return id_Person.get();}
    public String getRequires_food_donations(){return requires_food_donations.get();}
    public String getAccepted_pet_type(){return accepted_pet_type.get();}
    public String getAccepted_pet_size(){return accepted_pet_size.get();}
    public String getLocation(){return location.get();}

}
