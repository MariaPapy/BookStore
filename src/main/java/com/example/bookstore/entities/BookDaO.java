package com.example.bookstore.entities;

import lombok.Getter;

@Getter
public class BookDaO {
    protected int id;
    private String name;
    private String author;
    private String language;
    private int publishYear;
    private String genre;
    private String ISBN;
    private float price;
    private int pages;
    private String annotation;
    private Integer rating;
    private Boolean isNew;
    private int amount;
    private Integer cover;


    public int getId() {return id;}

    public String getName() {return name;}

    public String getAuthor() {return author;}

    public String getLanguage() {return language;}

    public int getPublishYear() {return publishYear;}

    public String getGenre() {return genre;}

    public String getISBN() {return ISBN;}

    public float getPrice() {return price;}

    public int getPages() {return pages;}

    public String getAnnotation() {return annotation;}

    public Integer getRating() {return rating;}

    public Boolean isNew() {return isNew;}

    public int getAmount() {return amount;}
    public Integer getCover() {return cover;}

    public BookDaO(int id, String name, String author, String language, int publishYear, String genre, String ISBN, float price, int pages, String annotation, Integer rating, Boolean isNew, int amount, Integer cover) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.language = language;
        this.publishYear = publishYear;
        this.genre = genre;
        this.ISBN = ISBN;
        this.price = price;
        this.pages = pages;
        this.annotation = annotation;
        this.rating = rating;
        this.isNew = isNew;
        this.amount = amount;
        this.cover = cover;
    }

}