package com.example.invoice;

public class Add_Items_Model {
    String id, name, rate, quantity, subtotal;

    public Add_Items_Model(String id, String name, String rate, String quantity, String subtotal) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getSubtotal() {
        return subtotal;
    }
}
