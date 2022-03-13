package com.movie.movie.dao.common;
/**
 * 影厅座位信息管理数据库操作层
 */

import com.movie.movie.entity.common.CinemaHallSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaHallSeatDao extends JpaRepository<CinemaHallSeat, Long> {

    List<CinemaHallSeat> findByCinemaHallId(Long cinemaHallId);
}
