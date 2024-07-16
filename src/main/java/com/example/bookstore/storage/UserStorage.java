package com.example.bookstore.storage;

import com.example.bookstore.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStorage extends CrudRepository<User, Integer> {

    List<User> findAll();

    User findById(int id);
    User findByLogin(String login);
    int curUser = 1;
    boolean existsByLogin(String login);

}