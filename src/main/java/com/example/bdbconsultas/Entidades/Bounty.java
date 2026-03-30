package com.example.bdbconsultas.Entidades;

import javafx.beans.property.SimpleIntegerProperty;

public class Bounty {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleIntegerProperty amount = new SimpleIntegerProperty();
    private SimpleIntegerProperty id_Pet = new SimpleIntegerProperty();
    private SimpleIntegerProperty id_Currency = new SimpleIntegerProperty();

    Bounty(int id, int amount, int id_Pet, int id_Currency) {
        this.id.set(id);
        this.amount.set(amount);
        this.id_Pet.set(id_Pet);
        this.id_Currency.set(id_Currency);
    }

    public int getId() {
        return id.get();
    }
    public int getAmount() {
        return amount.get();
    }
    public int getId_Pet() {
        return id_Pet.get();
    }
    public int getId_Currency() {
        return id_Currency.get();
    }
}
