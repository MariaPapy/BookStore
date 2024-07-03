package com.example.bookstore.service;

import com.example.bookstore.entities.Book;
import com.example.bookstore.entities.ClientCondition;
import com.example.bookstore.entities.BookDaO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StorageService {
    private static Map<Integer, Integer> cart = new HashMap<>();
    private DaOService daOService;
    @Autowired
    public void setDaOService(DaOService daOService) {
        this.daOService = daOService;
    }
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        List<BookDaO> booksDaO = null;
        try {
            booksDaO = daOService.getBooks();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (BookDaO bookDaO : booksDaO) {
            books.add(new Book(bookDaO.getId(), bookDaO.getName(), bookDaO.getAuthor(), bookDaO.getLanguage(), bookDaO.getPublishYear(), bookDaO.getGenre(), bookDaO.getISBN(), bookDaO.getPrice(), bookDaO.getPages(), bookDaO.getAnnotation(), bookDaO.getRating(), bookDaO.isNew(), bookDaO.getAmount(), bookDaO.getCover()));
        }
        return books;
    }

    public void addBook(BookDaO bookDaO) {
        daOService.addBook(bookDaO);
    }

    public Book getBook(int id) {
        BookDaO bookDaO = daOService.getBook(id);
        return new Book(bookDaO.getId(), bookDaO.getName(), bookDaO.getAuthor(), bookDaO.getLanguage(), bookDaO.getPublishYear(), bookDaO.getGenre(), bookDaO.getISBN(), bookDaO.getPrice(), bookDaO.getPages(), bookDaO.getAnnotation(), bookDaO.getRating(), bookDaO.isNew(), bookDaO.getAmount(), bookDaO.getCover());
    }


    public void addToCart(int id) {
        if (cart.containsKey(id)) {
            cart.replace(id,cart.get(id)+1);
        }
        else {
            cart.put(id,1);
        }
        System.out.println(cart.get(id));
    }

    public Object getCartAmount(int id) {
        if (cart.containsKey(id)) {
            return cart.get(id);
        }
        return 0;
    }

    public List<ClientCondition> getCartBooks() {
        List<ClientCondition> booksInCart = new ArrayList<>();
        for (Integer bookId : cart.keySet()) {
            BookDaO bd = daOService.getBook(bookId);
            ClientCondition cartPosition = new ClientCondition(bd.getId(), bd.getName(), bd.getAuthor(), bd.getPrice(), cart.get(bookId));
            booksInCart.add(cartPosition);
        }
        return booksInCart;
    }
    public float getCartTotal() {
        float total = 0;
        for (Integer bookId : cart.keySet()) {
            total +=  cart.get(bookId)*daOService.getBook(bookId).getPrice();
        }
        return (float) (Math.round(total * 100.0) / 100.0);
    }
    public List<String> getGenres() {
        return daOService.getGenres();
    }
    public List<String> getLanguages() {
        return daOService.getLanguages();
    }
}