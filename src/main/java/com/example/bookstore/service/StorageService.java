package com.example.bookstore.service;

import com.example.bookstore.entities.*;
import com.example.bookstore.user.*;
import com.example.bookstore.storage.BookStorage;
import com.example.bookstore.storage.CartStorage;
import com.example.bookstore.storage.UserStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class StorageService {
    private BCryptPasswordEncoder passwordEncoder;
    private BookStorage bookstorage;
    private UserStorage userstorage;
    private CartStorage cartstorage;
    @Autowired
    public void setBookstorage(BookStorage bookstorage) {
        this.bookstorage = bookstorage;
    }
    @Autowired
    public void setUserstorage(UserStorage userstorage) {
        this.userstorage = userstorage;
    }
    @Autowired
    public void setCart(CartStorage cartstorage) {
        this.cartstorage = cartstorage;
    }

    @Autowired
    @Qualifier("storageServicePasswordEncoder")
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        List<BookDaO> booksDaO;
        booksDaO = bookstorage.findAll();
        for (BookDaO bookDaO : booksDaO) {
            books.add(new Book(bookDaO.getId(), bookDaO.getName(), bookDaO.getAuthor(), bookDaO.getLanguage(), bookDaO.getPublishYear(), bookDaO.getGenre(), bookDaO.getISBN(), bookDaO.getPrice(), bookDaO.getPages(), bookDaO.getAnnotation(), bookDaO.getRating(), bookDaO.isNew(), bookDaO.getAmount(), bookDaO.getCover()));
        }
        return books;
    }

    public Book getBook(int id) {
        BookDaO bookDaO = bookstorage.findById(id);
        return new Book(bookDaO.getId(), bookDaO.getName(), bookDaO.getAuthor(), bookDaO.getLanguage(), bookDaO.getPublishYear(), bookDaO.getGenre(), bookDaO.getISBN(), bookDaO.getPrice(), bookDaO.getPages(), bookDaO.getAnnotation(), bookDaO.getRating(), bookDaO.isNew(), bookDaO.getAmount(), bookDaO.getCover());
    }

    public void subtractFromCart(int user_id, int book_id, int amount) {
        ConditionCartDaO oldConditionCartDaO = cartstorage.findByUserIdAndBookId(user_id, book_id);
        if (oldConditionCartDaO.getAmount() - amount <= 0) {
            cartstorage.delete(oldConditionCartDaO);
        }
        else {
            oldConditionCartDaO.setAmount(oldConditionCartDaO.getAmount()-amount);
            cartstorage.save(oldConditionCartDaO);
        }
    }

    public void clearCart(int user_id) {
        cartstorage.deleteByUserId(user_id);
    }

    public void addToCart(int user_id, int book_id) {
        ConditionCartDaO oldConditionCartDaO = cartstorage.findByUserIdAndBookId(user_id, book_id);
        if (oldConditionCartDaO != null) {
            oldConditionCartDaO.setAmount(oldConditionCartDaO.getAmount()+1);
            cartstorage.save(oldConditionCartDaO);
        }
        else {
            ConditionCartDaO newConditionCartDaO = new ConditionCartDaO(null, bookstorage.findById(book_id), 1, userstorage.findById(user_id));
            cartstorage.save(newConditionCartDaO);
        }
    }

    public Integer getCartAmount(int user_id, int book_id) {
        ConditionCartDaO oldConditionCartDaO = cartstorage.findByUserIdAndBookId(user_id, book_id);
        if (oldConditionCartDaO != null) {
            return oldConditionCartDaO.getAmount();
        }
        else {
            return 0;
        }
    }

    public List<ConditionCart> getCartBooks(int id) {
        List<ConditionCartDaO> cartPositionsDaO = cartstorage.findByUserId(id);
        List<ConditionCart> booksInCart = new ArrayList<>();
        for (ConditionCartDaO conditionCartDaO : cartPositionsDaO) {
            ConditionCart conditionCart = new ConditionCart(conditionCartDaO.getId(), conditionCartDaO.getBook(), conditionCartDaO.getAmount(), userstorage.findById(UserStorage.curUser));
            booksInCart.add(conditionCart);
        }
        return booksInCart;
    }
    public float getCartTotal(int id) {
        return cartstorage.getTotalPriceByUserId(id);
    }

    public void addUser(String username, String password) {
        userstorage.save(new User(null, username, passwordEncoder.encode(password), "ROLE_USER"));
    }


    public List<String> getGenres() {
        return bookstorage.getGenres();
    }
    public List<String> getLanguages() {
        return bookstorage.getLanguages();
    }

    public BookDaO addBook(BookDaO bookDaO) {
        return bookstorage.save(bookDaO);
    }

}