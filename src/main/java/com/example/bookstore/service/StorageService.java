package com.example.bookstore.service;

import com.example.bookstore.entities.Book;
import com.example.bookstore.entities.ClientCondition;
import com.example.bookstore.entities.BookDaO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

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
        List<BookDaO> booksDaO;
        booksDaO = daOService.findAll();
        for (BookDaO bookDaO : booksDaO) {
            books.add(new Book(bookDaO.getId(), bookDaO.getName(), bookDaO.getAuthor(), bookDaO.getLanguage(), bookDaO.getPublishYear(), bookDaO.getGenre(), bookDaO.getISBN(), bookDaO.getPrice(), bookDaO.getPages(), bookDaO.getAnnotation(), bookDaO.getRating(), bookDaO.isNew(), bookDaO.getAmount(), bookDaO.getCover()));
        }
        return books;
    }

    public BookDaO addBook(BookDaO bookDaO) {
        return daOService.save(bookDaO);
    }

    public Book getBook(int id) {
        BookDaO bookDaO = daOService.findById(id);
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

    public Integer getCartAmount(int id) {
        if (cart.containsKey(id)) {
            return cart.get(id);
        }
        return 0;
    }

    public List<ClientCondition> getCartBooks() {
        List<ClientCondition> booksInCart = new ArrayList<>();
        for (Integer bookId : cart.keySet()) {
            Optional<BookDaO> bd = daOService.findById(bookId);
            if (bd.isPresent()) {
                BookDaO book = bd.get();
                ClientCondition cartPosition = new ClientCondition(book.getId(), book.getName(), book.getAuthor(), book.getPrice(), cart.get(bookId));
                booksInCart.add(cartPosition);
            }
        }
        return booksInCart;
    }
    public float getCartTotal() {
        float total = 0;
        for (Integer bookId : cart.keySet()) {
            Optional<BookDaO> bd = daOService.findById(bookId);
            if (bd.isPresent()) {
                BookDaO book = bd.get();
                total += cart.get(bookId) * book.getPrice();
            }
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
