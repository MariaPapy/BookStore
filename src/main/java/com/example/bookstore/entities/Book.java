package com.example.bookstore.entities;

public class Book extends BookDaO {
    private String status;

    public Book(int id, String name, String author, String language, int publishYear, String genre, String ISBN, float price, int pages, String annotation, Integer rating, boolean isNew, int amount, Integer cover) {
        super(id, name, author, language, publishYear, genre, ISBN, price, pages, annotation, rating, isNew, amount, cover);
        if (amount < 5) {
            this.status = "Осталось мало, торопитесь!";
        }
        if (amount < 1) {
            this.status = "Товар закончился(";
        }
    }

    public String getStatus() {
        return status;
    }

}

