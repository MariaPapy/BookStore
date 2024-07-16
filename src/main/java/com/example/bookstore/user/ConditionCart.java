package com.example.bookstore.user;

import com.example.bookstore.entities.BookDaO;
import com.example.bookstore.entities.OrderDaO;
import lombok.NoArgsConstructor;


public class ConditionCart extends ConditionCartDaO {
    private final float total;

    public ConditionCart(Integer id, BookDaO book, int amount, OrderDaO order) {
        super(id, book, amount, order);
        this.total = (float) (Math.round(book.getPrice() * amount ));
    }

    public float getTotal() {
        return total;
    }

}