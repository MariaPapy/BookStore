package com.example.bookstore.service;

import com.example.bookstore.cart.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStorage extends CrudRepository<User, Integer> {

    List<User> findAll();

    User findById(int id);
    User findByLogin(String login);
    int curUser = 1;

}