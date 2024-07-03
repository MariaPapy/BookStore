package com.example.bookstore.service;

import com.example.bookstore.entities.BookDaO;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DaOService {
    private static final String URL = "jdbc:h2:mem:testdb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";


    public List<BookDaO> getBooks() throws SQLException {
        String sql = "SELECT * FROM books";
        List<BookDaO> books = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                books.add(mapResultSetToBook(resultSet));
            }
        }

        return books;
    }

    private BookDaO mapResultSetToBook(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        String author = resultSet.getString("author");
        String bookLanguage = resultSet.getString("book_language");
        int publishYear = resultSet.getInt("publish_year");
        String genre = resultSet.getString("genre");
        String isbn = resultSet.getString("isbn");
        float price = resultSet.getFloat("price");
        int pages = resultSet.getInt("pages");
        String annotation = resultSet.getString("annotation");
        Integer rating = null;
        int ratingValue = resultSet.getInt("rating");
        if (!resultSet.wasNull()) {
            rating = ratingValue;
        }
        boolean isNew = resultSet.getBoolean("is_new");
        int amount = resultSet.getInt("amount");
        Integer cover = null;
        int coverValue = resultSet.getInt("cover");
        if (!resultSet.wasNull()) {
            cover = coverValue;
        }

        return new BookDaO(id, name, author, bookLanguage, publishYear, genre, isbn, price, pages, annotation, rating, isNew, amount, cover);
    }

    private static List<String> genres = List.of("Роман","Повесть", "Детектив", "Научная фантастика");
    private static List<String> languages = List.of("Русский", "Английский", "Китайский", "Японский");


    public void addBook(BookDaO book) {
        String sql = "INSERT INTO BOOKS (NAME, AUTHOR, BOOK_LANGUAGE, PUBLISH_YEAR, GENRE, ISBN, PRICE, PAGES, ANNOTATION, RATING, IS_NEW, AMOUNT, COVER) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getLanguage());
            statement.setInt(4, book.getPublishYear());
            statement.setString(5, book.getGenre());
            statement.setString(6, book.getISBN());
            statement.setDouble(7, book.getPrice());
            statement.setInt(8, book.getPages());
            statement.setString(9, book.getAnnotation());
            statement.setInt(10, book.getRating() != null ? book.getRating() : 0);
            statement.setBoolean(11, book.isNew());
            statement.setInt(12, book.getAmount());
            statement.setInt(13, book.getCover() != null ? book.getCover() : 0);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
    }

    public BookDaO getBook(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToBook(resultSet);
            }
        }
        catch (SQLException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
        return null;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getLanguages() {
        return languages;
    }
}


