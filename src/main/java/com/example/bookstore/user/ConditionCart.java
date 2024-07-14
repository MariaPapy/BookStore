package com.example.bookstore.user;

import com.example.bookstore.entities.BookDaO;
import lombok.NoArgsConstructor;


public class ConditionCart extends ConditionCartDaO {
    private final float total;

    public ConditionCart(Integer id, BookDaO book, int amount, User user) {
        super(id, book, amount, user);
        this.total = (float) (Math.round(book.getPrice() * amount ));
    }

    public float getTotal() {
        return total;
    }

}