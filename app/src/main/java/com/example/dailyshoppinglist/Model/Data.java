package com.example.dailyshoppinglist.Model;

public class Data {

    String item;
    String quantity;
    String id;
    String unit;

    public Data() {
    }

    public Data(String item, String quantity, String unit, String id) {
        this.item = item;
        this.quantity = quantity;
        this.unit = unit;
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
