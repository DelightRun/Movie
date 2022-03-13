package com.movie.movie.service.common;

import com.movie.movie.bean.PageBean;
import com.movie.movie.dao.common.NewsDao;
import com.movie.movie.entity.common.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 新闻信息service层
 *
 * @author Administrator
 */
@Service
public class NewsService {

    @Autowired
    private NewsDao newsDao;

    /**
     * 当news的id不为空时进行编辑，当id为空时则进行添加
     *
     * @param news
     * @return
     */
    public News save(News news) {
        return newsDao.save(news);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public News find(Long id) {
        return newsDao.find(id);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(Long id) {
        newsDao.deleteById(id);
    }

    /**
     * 分页查找信息
     *
     * @param news
     * @param pageBean
     * @return
     */
    public PageBean<News> findPage(News news, PageBean<News> pageBean) {
        ExampleMatcher withMatcher = ExampleMatcher.matching().withMatcher("title", GenericPropertyMatchers.contains());
        withMatcher = withMatcher.withIgnorePaths("viewNumber");
        Example<News> example = Example.of(news, withMatcher);
        Pageable pageable = PageRequest.of(pageBean.getCurrentPage() - 1, pageBean.getPageSize());
        Page<News> findAll = newsDao.findAll(example, pageable);
        pageBean.setContent(findAll.getContent());
        pageBean.setTotal(findAll.getTotalElements());
        pageBean.setTotalPage(findAll.getTotalPages());
        return pageBean;
    }

    /**
     * 获取浏览量最高的两篇
     *
     * @return
     */
    public List<News> findTop() {
        return newsDao.findTop2ByOrderByViewNumberDesc();
    }

    /**
     * 获取所有的新闻列表
     *
     * @return
     */
    public List<News> findAll() {
        return newsDao.findAll();
    }

    /**
     * 获取某个分类下的所有新闻
     *
     * @param newsCategoryId
     * @return
     */
    public List<News> findAll(Long newsCategoryId) {
        return newsDao.findByNewsCategoryId(newsCategoryId);
    }
}
