package com.movie.movie.dao.common;
/**
 * 影厅信息管理数据库操作层
 */

import com.movie.movie.entity.common.CinemaHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaHallDao extends JpaRepository<CinemaHall, Long> {

    List<CinemaHall> findByCinemaId(Long cinemaId);
}
