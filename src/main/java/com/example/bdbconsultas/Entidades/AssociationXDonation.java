package com.example.bdbconsultas.Entidades;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AssociationXDonation {
    private SimpleIntegerProperty id_Association = new SimpleIntegerProperty();
    private SimpleIntegerProperty id_Donation = new SimpleIntegerProperty();

    public AssociationXDonation(int id_Association, int id_Donation) {
        this.id_Association.set(id_Association);
        this.id_Donation.set(id_Donation);
    }

    public int getId_Association() {return id_Association.get();}
    public int getId_Donation() {return id_Donation.get();}
}
