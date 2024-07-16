package com.example.bookstore.storage;

import com.example.bookstore.user.ConditionCart;
import com.example.bookstore.user.ConditionCartDaO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartStorage extends CrudRepository<ConditionCartDaO, Integer> {

    @Query("SELECT cp FROM ConditionCartDaO cp JOIN cp.order o JOIN o.user u WHERE u.id = :userId AND o.status = 'Формируется'")
    List<ConditionCartDaO> findByUserId(Integer userId);

    @Query("SELECT cp FROM ConditionCartDaO cp JOIN cp.order o JOIN o.user u WHERE u.id = :userId AND cp.book.id = :bookId AND o.status = 'Формируется'")
    ConditionCartDaO findByUserIdAndBookId(int userId, int bookId);

    @Query("SELECT COALESCE(SUM(cp.amount * b.price), 0) FROM ConditionCartDaO cp JOIN cp.order o JOIN o.user u JOIN cp.book b WHERE u.id = :userId AND o.status = 'Формируется'")
    Float getTotalPriceByUserId(Integer userId);

}