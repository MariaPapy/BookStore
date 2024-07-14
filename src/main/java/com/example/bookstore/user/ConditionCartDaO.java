package com.example.bookstore.user;

import com.example.bookstore.entities.BookDaO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
