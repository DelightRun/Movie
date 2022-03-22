package com.movie.movie.dao.common;
/**
 * 支付记录信息管理数据库操作层
 */

import com.movie.movie.entity.common.PayLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PayLogDao extends JpaRepository<PayLog, Long> {

    @Query("select pl from PayLog pl where pl.id = :id")
    PayLog find(@Param("id") Long id);

    PayLog findBySn(String sn);

    List<PayLog> findByAccountIdOrderByUpdateTimeDesc(Long accountId);

    List<PayLog> findByCreateTimeGreaterThan(Date createTime);

    List<PayLog> findByCreateTimeLessThanEqualAndStatus(Date createTime, Integer status);

    Long countByStatus(Integer status);

    @Modifying
    @Query("update PayLog set status = :newStatus where sn = :sn and status = :oldStatus")
    int updateStatus(@Param("sn") String sn, @Param("oldStatus") Integer oldStatus, @Param("newStatus") Integer newStatus);
}
