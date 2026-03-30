package com.example.bdbconsultas.Entidades;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Person {
    private SimpleIntegerProperty id = new SimpleIntegerProperty();
    private SimpleStringProperty FirstName = new SimpleStringProperty();
    private SimpleStringProperty SecondName = new SimpleStringProperty();
    private SimpleStringProperty FirstSurname = new SimpleStringProperty();
    private SimpleStringProperty SecondSurname = new SimpleStringProperty();
    private SimpleIntegerProperty idBlackList = new SimpleIntegerProperty();

    public Person(int id, String FirstName, String SecondName, String FirstSurname, int idBlackList) {}
}
