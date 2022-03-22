package com.movie.movie.dao.common;
/**
 * 影厅排片信息管理数据库操作层
 */

import com.movie.movie.entity.common.Cinema;
import com.movie.movie.entity.common.CinemaHallSession;
import com.movie.movie.entity.common.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaHallSessionDao extends JpaRepository<CinemaHallSession, Long> {

    @Query(value = "select count(id) from movie_cinema_hall_session where movie_id = :movieId and start_time > DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i') GROUP BY cinema_id", nativeQuery = true)
    List<Integer> getShowTotal(@Param("movieId") Long movieId);

    @Query(value = "select count(id) from movie_cinema_hall_session where cinema_id = :cinemaId and start_time > DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i') GROUP BY movie_id", nativeQuery = true)
    List<Integer> getCinemaShowTotal(@Param("cinemaId") Long cinemaId);

//    @Query(value = "select * from movie_cinema_hall_session where movie_id = :movieId and start_time > DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i')", nativeQuery = true)
//    List<CinemaHallSession> findDistinctCinemaList(@Param("movieId") Long movieId);
    @Query(value = "select distinct c.cinema from CinemaHallSession c where movie_id = :movieId and start_time > DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i')")
    List<Cinema> findDistinctCinemaList(@Param("movieId") Long movieId);

//    @Query(value = "select * from movie_cinema_hall_session where movie_id = :movieId and start_time > DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i') GROUP BY show_date,cinema_id", nativeQuery = true)
    @Query(value = "select distinct c.showDate from CinemaHallSession c where movie_id = :movieId and start_time > DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i')")
    List<String> findDistinctShowDateList(@Param("movieId") Long movieId);

    List<CinemaHallSession> findByMovieIdAndCinemaIdAndShowDate(Long movieId, Long cinemaId, String showDate);

    @Query("select count(id) from CinemaHallSession where id <> :id and cinemaHall.id = :cinemaHallId and showDate = :showDate and ((startTime <= :startTime and endTime >= :startTime) or (startTime <= :endTime and endTime >= :endTime))")
    int getHallCount(@Param("id") Long id, @Param("cinemaHallId") Long cinemaHallId, @Param("showDate") String showDate, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "select distinct c.showDate from CinemaHallSession c where c.cinema.id = :cinemaId and start_time > DATE_FORMAT(NOW(),'%Y-%m-%d %H:%i')")
    List<String> findDistinctShowDateByCinemaList(@Param("cinemaId") Long cinemaId);

    @Query(value = "select distinct c.movie from CinemaHallSession c where c.cinema.id = :cinemaId and c.showDate = :showDate")
    List<Movie> findDistinctMovieByCinemaList(@Param("cinemaId") Long cinemaId, @Param("showDate") String showDate);

}
