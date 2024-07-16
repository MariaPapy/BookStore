package com.example.bookstore.user;

import com.example.bookstore.entities.BookDaO;
import com.example.bookstore.entities.OrderDaO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "condition_cart")
public class ConditionCartDaO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookDaO book;
    private int amount;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderDaO order;


    public ConditionCartDaO(Integer id, BookDaO book, int amount, OrderDaO order) {
        this.id = id;
        this.book = book;
        this.amount = amount;
        this.order = order;
    }

}
