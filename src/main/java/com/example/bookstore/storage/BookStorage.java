package com.example.bookstore.storage;

import com.example.bookstore.entities.BookDaO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface BookStorage extends CrudRepository<BookDaO, Integer> {

    List<BookDaO> findAll();

    BookDaO findById(int id);

}