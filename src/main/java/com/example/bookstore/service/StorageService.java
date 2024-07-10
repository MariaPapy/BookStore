package com.example.bookstore.service;

import com.example.bookstore.entities.*;
import com.example.bookstore.cart.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class StorageService {
    private BookStorage bookRepository;
    private UserStorage userRepository;
    private CartStorage cartRepository;
    @Autowired
    public void setBookRepository(BookStorage bookRepository) {
        this.bookRepository = bookRepository;
    }
    @Autowired
    public void setUserRepository(UserStorage userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setCart(CartStorage cartRepository) {
        this.cartRepository = cartRepository;
    }
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        List<BookDaO> booksDaO;
        booksDaO = bookRepository.findAll();
        for (BookDaO bookDaO : booksDaO) {
            books.add(new Book(bookDaO.getId(), bookDaO.getName(), bookDaO.getAuthor(), bookDaO.getLanguage(), bookDaO.getPublishYear(), bookDaO.getGenre(), bookDaO.getISBN(), bookDaO.getPrice(), bookDaO.getPages(), bookDaO.getAnnotation(), bookDaO.getRating(), bookDaO.isNew(), bookDaO.getAmount(), bookDaO.getCover()));
        }
        return books;
    }

    public BookDaO addBook(BookDaO bookDaO) {
        return bookRepository.save(bookDaO);
    }

    public Book getBook(int id) {
        BookDaO bookDaO = bookRepository.findById(id);
        return new Book(bookDaO.getId(), bookDaO.getName(), bookDaO.getAuthor(), bookDaO.getLanguage(), bookDaO.getPublishYear(), bookDaO.getGenre(), bookDaO.getISBN(), bookDaO.getPrice(), bookDaO.getPages(), bookDaO.getAnnotation(), bookDaO.getRating(), bookDaO.isNew(), bookDaO.getAmount(), bookDaO.getCover());
    }

    public void subtractFromCart(int user_id, int book_id, int amount) {
        ConditionCartDaO oldConditionCartDaO = cartRepository.findByUserIdAndBookId(user_id, book_id);
        if (oldConditionCartDaO.getAmount()-amount <= 0) {
            cartRepository.delete(oldConditionCartDaO);
        }
        else {
            oldConditionCartDaO.setAmount(oldConditionCartDaO.getAmount()-amount);
            cartRepository.save(oldConditionCartDaO);
        }
    }
    public void addToCart(int user_id, int book_id) {
        ConditionCartDaO oldConditionCartDaO = cartRepository.findByUserIdAndBookId(user_id, book_id);
        if (oldConditionCartDaO != null) {
            oldConditionCartDaO.setAmount(oldConditionCartDaO.getAmount()+1);
            cartRepository.save(oldConditionCartDaO);
        }
        else {
            ConditionCartDaO newConditionCartDaO = new ConditionCartDaO(null, bookRepository.findById(book_id), 1, userRepository.findById(user_id));
            cartRepository.save(newConditionCartDaO);
        }
    }

    public Integer getCartAmount(int user_id, int book_id) {
        ConditionCartDaO oldConditionCartDaO = cartRepository.findByUserIdAndBookId(user_id, book_id);
        if (oldConditionCartDaO != null) {
            return oldConditionCartDaO.getAmount();
        }
        else {
            return 0;
        }
    }

    public List<ConditionConditionCart> getCartBooks(int id) {
        List<ConditionCartDaO> cartPositionsDaO = cartRepository.findByUserId(id);
        List<ConditionConditionCart> booksInCart = new ArrayList<>();
        for (ConditionCartDaO conditionCartDaO : cartPositionsDaO) {
            ConditionConditionCart conditionCart = new ConditionConditionCart(conditionCartDaO.getId(), conditionCartDaO.getBook(), conditionCartDaO.getAmount(), userRepository.findById(UserStorage.curUser));
            booksInCart.add(conditionCart);
        }
        return booksInCart;
    }
    public float getCartTotal(int id) {
        return cartRepository.getTotalPriceByUserId(id);
    }
    public List<String> getGenres() {
        return bookRepository.getGenres();
    }
    public List<String> getLanguages() {
        return bookRepository.getLanguages();
    }
}