package com.example.bookstore.cart;

import com.example.bookstore.entities.BookDaO;

public class ConditionCart extends ConditionCartDaO {
    private final float total;

    public ConditionCart(Integer id, BookDaO book, int amount, User user) {
        super(id, book, amount, user);
        this.total = (float) (Math.round(book.getPrice() * amount * 100.0) / 100.0);
    }

    public float getTotal() {
        return total;
    }

}