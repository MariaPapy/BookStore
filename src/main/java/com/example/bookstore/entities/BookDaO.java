package com.example.bookstore.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="books")
public class BookDaO implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    private String name;
    private String author;
    @Column(name="book_language")
    private String language;
    @Column(name="publish_year")
    private int publishYear;
    private String genre;
    private String ISBN;
    private float price;
    private int pages;
    private String annotation;
    private Integer rating;
    @Column(name="is_new")
    private Boolean isNew;
    private int amount;
    private Integer cover;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookDaO)) return false;
        BookDaO book = (BookDaO) o;
        return getPublishYear() == book.getPublishYear() &&
                getPages() == book.getPages() &&
                getAmount() == book.getAmount() &&
                Objects.equals(getName(), book.getName()) &&
                Objects.equals(getAuthor(), book.getAuthor()) &&
                Objects.equals(getLanguage(), book.getLanguage()) &&
                Objects.equals(getGenre(), book.getGenre()) &&
                Objects.equals(getISBN(), book.getISBN()) &&
                Float.compare(book.getPrice(), getPrice()) == 0 &&
                Objects.equals(getAnnotation(), book.getAnnotation()) &&
                Objects.equals(getRating(), book.getRating()) &&
                Objects.equals(isNew(), book.isNew()) &&
                Objects.equals(getCover(), book.getCover());
    }

    public Boolean isNew() {
        return isNew;
    }

}