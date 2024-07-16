package com.example.bookstore.service;

import com.example.bookstore.entities.*;
import com.example.bookstore.user.*;
import com.example.bookstore.storage.BookStorage;
import com.example.bookstore.storage.CartStorage;
import com.example.bookstore.storage.UserStorage;
import com.example.bookstore.storage.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class StorageService {
    private BCryptPasswordEncoder passwordEncoder;
    private BookStorage bookstorage;
    private UserStorage userstorage;
    private CartStorage cartstorage;
    private OrderRepository orderRepository;
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
    public void setOrderRepository(OrderRepository orderRepository) {this.orderRepository = orderRepository;}

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

    @Transactional
    public void subtractFromCart(int user_id, int book_id, int amount) {
        ConditionCartDaO oldCartPositionDaO = cartstorage.findByUserIdAndBookId(user_id, book_id);
        if (oldCartPositionDaO.getAmount()-amount <= 0) {
            cartstorage.delete(oldCartPositionDaO);
            OrderDaO order = orderRepository.findCurrentOrderByUserId(user_id);
            if (order != null && order.getCartPositions().size() == 1) {
                orderRepository.delete(order);
            }
        }
        else {
            oldCartPositionDaO.setAmount(oldCartPositionDaO.getAmount()-amount);
            cartstorage.save(oldCartPositionDaO);
        }
    }

 /*   public void clearCart(int user_id) {
        cartstorage.deleteByUserId(user_id);
    } */

    @Transactional
    public void addToCart(int user_id, int book_id) {
        ConditionCartDaO oldCartPositionDaO = cartstorage.findByUserIdAndBookId(user_id, book_id);
        if (oldCartPositionDaO != null) {
            oldCartPositionDaO.setAmount(oldCartPositionDaO.getAmount()+1);
            cartstorage.save(oldCartPositionDaO);
        }
        else {
            OrderDaO order = orderRepository.findCurrentOrderByUserId(user_id);
            if (order == null) {
                order = orderRepository.save(new OrderDaO(null, new ArrayList<>(), "Формируется", userstorage.findById(user_id)));
            }
            ConditionCartDaO newCartPositionDaO = new ConditionCartDaO(null, bookstorage.findById(book_id), 1, order);
            order.getCartPositions().add(newCartPositionDaO);
            cartstorage.save(newCartPositionDaO);
            orderRepository.save(order);
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
        OrderDaO order = orderRepository.findCurrentOrderByUserId(id);
        for (ConditionCartDaO cartPositionDaO : cartPositionsDaO) {
            ConditionCart cartPosition = new ConditionCart(cartPositionDaO.getId(), cartPositionDaO.getBook(), cartPositionDaO.getAmount(), order);
            booksInCart.add(cartPosition);
        }
        return booksInCart;
    }


    @Transactional
    public boolean makeOrder(int userId) {
        OrderDaO order = orderRepository.findCurrentOrderByUserId(userId);

        // Проверяем наличие книг на складе
        boolean hasChanged = false;
        for (ConditionCartDaO cartPositionDaO : order.getCartPositions()) {
            BookDaO book = bookstorage.findById(cartPositionDaO.getBook().getId()).get();
            if (book.getAmount() < cartPositionDaO.getAmount()) {
                hasChanged = true;
                // Если количество книг на складе недостаточно, уменьшаем количество в корзине
                cartPositionDaO.setAmount(Math.max(0, book.getAmount()));
                cartstorage.save(cartPositionDaO);
            }
        }

        if (!hasChanged) {
            // Если все книги есть на складе, оформляем заказ
            for (ConditionCartDaO cartPositionDaO : order.getCartPositions()) {
                BookDaO book = bookstorage.findById(cartPositionDaO.getBook().getId()).get();
                book.setAmount(book.getAmount() - cartPositionDaO.getAmount());
                bookstorage.save(book);
            }
            order.setStatus("Заказ оформлен");
            order.setFinished();
            orderRepository.save(order);
        } else {
            // Если количество книг в корзине было изменено, удаляем позиции с нулевым количеством
            order.getCartPositions().removeIf(cartPositionDaO -> cartPositionDaO.getAmount() == 0);
            if (order.getCartPositions().isEmpty()) {
                orderRepository.delete(order);
            } else {
                orderRepository.save(order);
            }
        }

        return !hasChanged;
    }


    public float getCartTotal(int id) {
        return cartstorage.getTotalPriceByUserId(id);
    }

    public boolean addUser(String username, String password) {
        if (userstorage.existsByLogin(username)) {
            return false;
        }
        else {
            userstorage.save(new User(null, username, passwordEncoder.encode(password), "ROLE_USER"));
            return true;
        }
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

    public List<OrderDaO> getOrders(int id) {
        return orderRepository.findByUserId(id);
    }

    public User getUser(int id) {
        return  userstorage.findById(id);
    }

    public List<OrderDaO> getOrderById(Integer orderId, Integer userId) {
        return orderRepository.findByIdAndUserId(orderId, userId);
    }

    public List<OrderDaO> getAllOrders() {
        return (List<OrderDaO>) orderRepository.findAll();
    }
}