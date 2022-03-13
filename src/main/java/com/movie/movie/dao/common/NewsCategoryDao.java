package com.movie.movie.dao.common;
/**
 * 新闻分类信息管理数据库操作层
 */

import com.movie.movie.entity.common.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsCategoryDao extends JpaRepository<NewsCategory, Long> {

    @Query("select nc from NewsCategory nc where nc.id = :id")
    NewsCategory find(@Param("id") Long id);
}
