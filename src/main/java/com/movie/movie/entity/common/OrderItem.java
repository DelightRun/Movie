package com.movie.movie.entity.common;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 订单子项实体
 *
 * @author Administrator
 */
@Entity
@Table(name = "movie_order_item")
@EntityListeners(AuditingEntityListener.class)
public class OrderItem extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;//所属订单

    @ManyToOne
    @JoinColumn(name = "cinema_hall_seat_id")
    private CinemaHallSeat cinemaHallSeat;//所属座位

    @Column(name = "money", nullable = false, length = 12)
    private BigDecimal money;//影票金额

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public CinemaHallSeat getCinemaHallSeat() {
        return cinemaHallSeat;
    }

    public void setCinemaHallSeat(CinemaHallSeat cinemaHallSeat) {
        this.cinemaHallSeat = cinemaHallSeat;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "OrderItem [order=" + order + ", cinemaHallSeat="
                + cinemaHallSeat + ", money=" + money + "]";
    }


}
