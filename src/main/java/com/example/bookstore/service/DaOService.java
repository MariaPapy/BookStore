package com.example.bookstore.service;

import com.example.bookstore.entities.BookDaO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface DaOService extends CrudRepository<BookDaO, Integer> {
    List<BookDaO> findAll();

    BookDaO findById(int integer);

    List<String> genres = List.of("Роман", "Фэнтези", "Детектив", "Научная фантастика");
    List<String> languages = List.of("Русский", "Английский", "Французский", "Итальянский");

    default List<String> getGenres() {
        return genres;
    }

    default List<String> getLanguages() {
        return languages;
    }
}
