package com.example.bookstore;

import com.example.bookstore.entities.BookDaO;
import com.example.bookstore.user.User;
import com.example.bookstore.storage.BookStorage;
import com.example.bookstore.storage.UserStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BookStore {

    @Bean(name="storageServicePasswordEncoder")
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(BookStore.class, args);
    }

    @Bean
    public CommandLineRunner init(BookStorage bookStorage, UserStorage userStorage, @Autowired BCryptPasswordEncoder passwordEncoder) {
        return (args) -> {
            userStorage.save(new User(null, "admin", passwordEncoder.encode("000"), "ROLE_ADMIN"));

            bookStorage.save(new BookDaO(null,
                    "1984",
                    "Джордж Оруэлл",
                    "Английский",
                    2023,
                    "Антиутопия",
                    "978-5-04-113136-7",
                    350,
                    328,
                    "Классическая антиутопия о тоталитарном обществе будущего, где свобода слова и мысли подавлена.",
                    8,
                    false,
                    2,
                    1));

            bookStorage.save(new BookDaO(null,
                    "Мастер и Маргарита",
                    "Михаил Булгаков",
                    "Русский",
                    2021,
                    "Роман",
                    "978-5-04-094927-5",
                    400,
                    432,
                    "Философский роман о противостоянии добра и зла в Москве 1930-х годов.",
                    9,
                    true,
                    40,
                    2));

            bookStorage.save(new BookDaO(null,
                    "Маленький принц",
                    "Антуан де Сент-Экзюпери",
                    "Французский",
                    2022,
                    "Детская литература",
                    "978-5-04-100100-1",
                    250,
                    96,
                    "Классическая история о дружбе, любви и важности простоты.",
                    10,
                    true,
                    3,
                    3));

            bookStorage.save(new BookDaO(null,
                    "Портрет Дориана Грея",
                    "Оскар Уайльд",
                    "Английский",
                    2013,
                    "Роман",
                    "978-5-04-117891-7",
                    350,
                    256,
                    "История о молодом человеке, который продает свою душу ради вечной молодости.",
                    8,
                    false,
                    50,
                    4));

            bookStorage.save(new BookDaO(null,
                    "Триумфальная арка",
                    "Эрих Мария Ремарк",
                    "Немецкий",
                    2022,
                    "Роман",
                    "978-5-04-102128-8",
                    450,
                    528,
                    "Роман о любви и жизни в Париже после Первой мировой войны.",
                    9,
                    false,
                    7,
                    5));

            bookStorage.save(new BookDaO(null,
                    "Унесенные ветром",
                    "Маргарет Митчелл",
                    "Английский",
                    1936,
                    "Роман",
                    "978-0-06-078900-8",
                    400,
                    1037,
                    "Эпическая история о любви, войне и жизни на юге США во время Гражданской войны.",
                    10,
                    false,
                    100,
                    7));

            bookStorage.save(new BookDaO(null,
                    "Властелин колец",
                    "Джон Р. Р. Толкин",
                    "Английский",
                    1954,
                    "Фэнтези",
                    "978-0-618-05326-7",
                    500,
                    1216,
                    "Эпическое фэнтези о борьбе добра и зла в мире Средиземья.",
                    10,
                    true,
                    50,
                    8));

            bookStorage.save(new BookDaO(null,
                    "Преступление и наказание",
                    "Фёдор Достоевский",
                    "Русский",
                    1866,
                    "Роман",
                    "978-5-04-113500-6",
                    300,
                    592,
                    "Философский роман о преступлении, наказании и поисках смысла жизни.",
                    9,
                    false,
                    80,
                    9));
            userStorage.save(new User(null, "user1", passwordEncoder.encode("000"), "ROLE_USER"));
            userStorage.save(new User(null, "user2", passwordEncoder.encode("000"), "ROLE_USER"));

        };
    }
}