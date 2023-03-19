package com.example.savvysaver.Adapter;

public class Expense {
    private String id;
    private String name;
    private String amount;
    private String type;
    private String description;

    public Expense(String id, String name, String amount, String type, String description) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return "Nombre: " + name;
    }

    public String getAmount() {
        return "Cantidad: " + amount + "€";
    }

    public String getType() {
        return "Categoría: " + type;
    }

    public String getDescription() {
        return "Descripcion: " + description;
    }
}
