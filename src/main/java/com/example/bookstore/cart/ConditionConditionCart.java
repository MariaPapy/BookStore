package com.example.bookstore.cart;

import com.example.bookstore.entities.BookDaO;

public class ConditionConditionCart extends ConditionCartDaO {
    private final float total;

    public ConditionConditionCart(Integer id, BookDaO book, int amount, User user) {
        super(id, book, amount, user);
        this.total = (float) (Math.round(book.getPrice() * amount ));
    }

    public float getTotal() {
        return total;
    }

}