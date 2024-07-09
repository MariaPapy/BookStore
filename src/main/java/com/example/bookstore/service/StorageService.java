package com.example.bookstore.service;

import com.example.bookstore.cart.ConditionCart;
import com.example.bookstore.cart.ConditionCartDaO;
import com.example.bookstore.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class StorageService {
    private BookStorage bookStorage;
    private UserStorage userStorage;
    private CartStorage cartStorage;
    @Autowired
    public void setBookRepository(BookStorage bookStorage) {
        this.bookStorage = bookStorage;
    }
    @Autowired
    public void setUserRepository(UserStorage userStorage) {
        this.userStorage = userStorage;
    }
    @Autowired
    public void setCart(CartStorage cartStorage) {
        this.cartStorage = cartStorage;
    }
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        List<BookDaO> booksDaO;
        booksDaO = bookStorage.findAll();
        for (BookDaO bookDaO : booksDaO) {
            books.add(new Book(bookDaO.getId(), bookDaO.getName(), bookDaO.getAuthor(), bookDaO.getLanguage(), bookDaO.getPublishYear(), bookDaO.getGenre(), bookDaO.getISBN(), bookDaO.getPrice(), bookDaO.getPages(), bookDaO.getAnnotation(), bookDaO.getRating(), bookDaO.isNew(), bookDaO.getAmount(), bookDaO.getCover()));
        }
        return books;
    }

    public BookDaO addBook(BookDaO bookDaO) {
        return bookStorage.save(bookDaO);
    }

    public Book getBook(int id) {
        BookDaO bookDaO = bookStorage.findById(id);
        return new Book(bookDaO.getId(), bookDaO.getName(), bookDaO.getAuthor(), bookDaO.getLanguage(), bookDaO.getPublishYear(), bookDaO.getGenre(), bookDaO.getISBN(), bookDaO.getPrice(), bookDaO.getPages(), bookDaO.getAnnotation(), bookDaO.getRating(), bookDaO.isNew(), bookDaO.getAmount(), bookDaO.getCover());
    }


    public void addToCart(int user_id, int book_id) {
        ConditionCartDaO oldConditionCartDaO = cartStorage.findByUserIdAndBookId(user_id, book_id);
        if (oldConditionCartDaO != null) {
            oldConditionCartDaO.setAmount(oldConditionCartDaO.getAmount()+1);
            cartStorage.save(oldConditionCartDaO);
        }
        else {
            ConditionCartDaO newConditionCartDaO = new ConditionCartDaO(null, bookStorage.findById(book_id), 1, userStorage.findById(user_id));
            cartStorage.save(newConditionCartDaO);
        }
    }

    public Integer getCartAmount(int user_id, int book_id) {
        ConditionCartDaO oldConditionCartDaO = cartStorage.findByUserIdAndBookId(user_id, book_id);
        if (oldConditionCartDaO != null) {
            return oldConditionCartDaO.getAmount();
        }
        else {
            return 0;
        }
    }

    public List<ConditionCart> getCartBooks(int id) {
        List<ConditionCartDaO> cartPositionsDaO = cartStorage.findByUserId(id);
        List<ConditionCart> booksInCart = new ArrayList<>();
        for (ConditionCartDaO conditionCartDaO : cartPositionsDaO) {
            ConditionCart conditionCart = new ConditionCart(conditionCartDaO.getId(), conditionCartDaO.getBook(), conditionCartDaO.getAmount(), userStorage.findById(UserStorage.curUser));
            booksInCart.add(conditionCart);
        }
        return booksInCart;
    }
    public float getCartTotal(int id) {
        return cartStorage.getTotalPriceByUserId(id);
    }
    public List<String> getGenres() {
        return bookStorage.getGenres();
    }
    public List<String> getLanguages() {
        return bookStorage.getLanguages();
    }
}