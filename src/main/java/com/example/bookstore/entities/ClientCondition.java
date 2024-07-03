package com.example.bookstore.entities;

public class ClientCondition {
    private int id;
    private String name;
    private String author;
    private float price;
    private int amount;
    private float total;

    public ClientCondition(int id, String name, String author, float price, int amount) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
        this.amount = amount;
        this.total = (float) (Math.round(price * amount * 100.0) / 100.0);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public float getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public float getTotal() {
        return total;
    }

}