package com.movie.movie.dao.common;
/**
 * 订单子项信息管理数据库操作层
 */

import com.movie.movie.entity.common.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemDao extends JpaRepository<OrderItem, Long> {

    @Query("select o from OrderItem o where o.id = :id")
    OrderItem find(@Param("id") Long id);

    List<OrderItem> findByOrderId(Long orderId);

    @Query("select o from OrderItem o where o.order.id IN :orderIds")
    List<OrderItem> findList(@Param("orderIds") List<Long> orderIds);

}
