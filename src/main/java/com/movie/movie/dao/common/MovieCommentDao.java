package com.movie.movie.dao.common;
/**
 * 电影评价信息管理数据库操作层
 */

import com.movie.movie.entity.common.MovieComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieCommentDao extends JpaRepository<MovieComment, Long> {

    List<MovieComment> findByAccountIdOrderByCreateTimeDesc(Long accountId);

    List<MovieComment> findByMovieIdOrderByCreateTimeDesc(Long movieId);

}
