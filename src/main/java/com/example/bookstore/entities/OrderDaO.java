package com.example.bookstore.entities;

import com.example.bookstore.user.ConditionCartDaO;
import com.example.bookstore.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name="orders")
public class OrderDaO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany(mappedBy = "order")
    private List<ConditionCartDaO> cartPositions;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String status;

    @Column(name = "order_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTime;

    private String desc;
    private float total;
    public OrderDaO() {

    }

    public OrderDaO(Integer id, List<ConditionCartDaO> cartPositions, String status, User user) {
        this.id = id;
        this.cartPositions = cartPositions;
        this.status = status;
        this.user = user;
    }


    public List<ConditionCartDaO> getCartPositions() {
        return cartPositions;
    }

    public void setCartPositions(List<ConditionCartDaO> cartPositions) {
        this.cartPositions = cartPositions;
    }

    public void setFinished() {
        setOrderTime(new java.util.Date());
        setStatus("Заказ оформлен");
        StringBuilder text = new StringBuilder();
        float price = 0;
        for (ConditionCartDaO cart : this.cartPositions) {
            text.append("'").append(cart.getBook().getName()).append("', ").append(cart.getBook().getAuthor()).append(" | x ").append(cart.getAmount()).append(" || ");
            price += cart.getBook().getPrice() * cart.getAmount();
        }
        setDesc(text.toString());
        setTotal(price);
    }
}

