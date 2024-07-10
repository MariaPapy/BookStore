package com.example.bookstore.service;

import com.example.bookstore.cart.ConditionCartDaO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartStorage extends CrudRepository<ConditionCartDaO, Integer> {

    List<ConditionCartDaO> findByUserId(Integer userId);

    ConditionCartDaO findByUserIdAndBookId(int curUser, int id);

    @Query("SELECT COALESCE(SUM(cp.amount * b.price), 0) FROM ConditionCartDaO cp JOIN cp.book b WHERE cp.user.id = :userId")
    Float getTotalPriceByUserId(Integer userId);
}