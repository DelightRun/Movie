package com.movie.movie.dao.common;
/**
 * 订单信息管理数据库操作层
 */

import com.movie.movie.entity.common.Order;
import org.apache.mahout.cf.taste.model.Preference;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    @Query("select o from Order o where o.id = :id")
    Order find(@Param("id") Long id);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Order findBySn(String sn);

    List<Order> findByAccountIdOrderByUpdateTimeDesc(Long accountId);

    List<Order> findByAccountIdAndStatus(Long accountId, Integer status);

    List<Order> findByAccountIdAndStatusOrderByUpdateTimeDesc(Long accountId, Integer status);

    List<Order> findByCinemaHallSessionIdAndStatusNot(Long cinemaHallSessionId, Integer status);

    List<Order> findByCreateTimeLessThanEqualAndStatus(Date outTime, Integer status);

    @Query("select o from Order o where o.cinemaHallSession.movie.id = :movieId and o.status = :status")
    List<Order> findByMovieIdAndStatus(@Param("movieId") Long movieId, @Param("status") Integer status);

    @Query("select o from Order o where o.account.id = :accountId and o.cinemaHallSession.movie.id = :movieId and o.status = :status")
    List<Order> findByAccountIdAndMovieIdAndStatus(@Param("accountId") Long accountId, @Param("movieId") Long movieId, @Param("status") Integer status);

    @Query("select distinct o.account.id from Order o where o.status = :status")
    List<Long> findDistinctAccountIdsByStatus(@Param("status") Integer status);

    @Query("select distinct o.cinemaHallSession.movie.id from Order o where o.status = :status")
    List<Long> findDistinctMovieIdsByStatus(@Param("status") Integer status);

    @Query("select distinct o.cinemaHallSession.movie.id from Order o where o.account.id = :accountId and o.status = :status")
    List<Long> findDistinctMovieIdsByAccountIdAndStatus(@Param("accountId") Long accountId, @Param("status") Integer status);

    @Query("select distinct new com.movie.movie.recommend.GenericBoolPreference(o.account.id, o.cinemaHallSession.movie.id) from Order o where o.status = 1")
    List<Preference> findPreferencesFromPaidOrders();

    int countByAccountIdAndStatus(Long accountId, Integer status);

    @Query("select count(o) from Order o where o.cinemaHallSession.movie.id = :movieId and o.status = :status")
    int countByMovieIdAndStatus(@Param("movieId") Long movieId, @Param("status") Integer status);

    @Modifying
    @Query("update Order set status = :newStatus where sn = :sn and status = :oldStatus")
    int updateOrderStatus(@Param("sn") String sn, @Param("oldStatus") Integer oldStatus, @Param("newStatus") Integer newStatus);
}
