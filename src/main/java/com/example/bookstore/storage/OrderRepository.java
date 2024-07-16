package com.example.bookstore.storage;

import com.example.bookstore.entities.OrderDaO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<OrderDaO, Integer> {

    @Query("SELECT o FROM OrderDaO o WHERE o.status = 'Формируется' AND o.user.id = :userId")
    OrderDaO findCurrentOrderByUserId(int userId);

    @Query("SELECT o FROM OrderDaO o WHERE o.user.id = :userId")
    List<OrderDaO> findByUserId(int userId);


    @Query("SELECT o FROM OrderDaO o WHERE o.id = :orderId AND o.user.id = :userId")
    List<OrderDaO> findByIdAndUserId(Integer orderId, Integer userId);
}