package com.movie.movie.dao.common;
/**
 * 影院评价信息管理数据库操作层
 */

import com.movie.movie.entity.common.CinemaComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CinemaCommentDao extends JpaRepository<CinemaComment, Long> {

    List<CinemaComment> findByAccountIdOrderByCreateTimeDesc(Long accountId);

    List<CinemaComment> findByCinemaIdOrderByCreateTimeDesc(Long cinemaId);

}
