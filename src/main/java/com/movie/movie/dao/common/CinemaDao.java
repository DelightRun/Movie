package com.movie.movie.dao.common;
/**
 * 电影院信息管理数据库操作层
 */

import com.movie.movie.entity.common.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaDao extends JpaRepository<Cinema, Long> {

    List<Cinema> findTop6ByOrderByRateDesc();

    List<Cinema> findByAreaCityId(Long cityId);

}
