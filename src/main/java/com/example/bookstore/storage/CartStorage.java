package com.example.bookstore.storage;

import com.example.bookstore.user.ConditionCartDaO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartStorage extends CrudRepository<ConditionCartDaO, Integer> {

    List<ConditionCartDaO> findByUserId(Integer userId);

    ConditionCartDaO findByUserIdAndBookId(int curUser, int id);

    @Query("SELECT COALESCE(SUM(cp.amount * b.price), 0) FROM ConditionCartDaO cp JOIN cp.book b WHERE cp.user.id = :userId")
    Float getTotalPriceByUserId(Integer userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ConditionCartDaO c WHERE c.user.id = :userId")
    void deleteByUserId(int userId);
}