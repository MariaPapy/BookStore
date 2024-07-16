package com.example.bookstore;

import com.example.bookstore.entities.BookDaO;
import com.example.bookstore.user.User;
import com.example.bookstore.storage.BookStorage;
import com.example.bookstore.storage.UserStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class BookStoreTest {

    @Autowired
    private BookStorage bookStorage;

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void testInitData() {
        // Проверка наличия администратора
        User admin = userStorage.findByLogin("admin");
        assertNotNull(admin);
        assertEquals("admin", admin.getUsername());

        // Проверка наличия книг
        BookDaO book1984 = bookStorage.findById(1);
        assertNotNull(book1984);
        assertEquals("1984", book1984.getName());

    }
}


