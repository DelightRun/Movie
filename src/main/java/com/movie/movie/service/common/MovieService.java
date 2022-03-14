package com.movie.movie.service.common;

import com.movie.movie.bean.PageBean;
import com.movie.movie.constant.SessionConstant;
import com.movie.movie.dao.common.MovieDao;
import com.movie.movie.entity.common.Account;
import com.movie.movie.entity.common.Movie;
import com.movie.movie.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 电影信息service层
 *
 * @author Administrator
 */
@Service
public class MovieService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderService orderService;
    @Autowired
    private MovieDao movieDao;
    @Autowired
    private RecommendService recommendService;

    /**
     * 当movie的id不为空时进行编辑，当id为空时则进行添加
     *
     * @param movie
     * @return
     */
    public Movie save(Movie movie) {
        return movieDao.save(movie);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public Movie findById(Long id) {
        return movieDao.getOne(id);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(Long id) {
        movieDao.deleteById(id);
    }

    /**
     * 分页查找电影列表信息
     *
     * @param movie
     * @param pageBean
     * @return
     */
    public PageBean<Movie> findPage(Movie movie, PageBean<Movie> pageBean) {
        ExampleMatcher withMatcher = ExampleMatcher.matching().withMatcher("name", GenericPropertyMatchers.contains());
        withMatcher = withMatcher.withIgnorePaths("isShow", "totalMoney", "rate", "rateCount");
        Example<Movie> example = Example.of(movie, withMatcher);
        Sort sort = Sort.by(Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(pageBean.getCurrentPage() - 1, pageBean.getPageSize(), sort);
        Page<Movie> findAll = movieDao.findAll(example, pageable);
        pageBean.setContent(findAll.getContent());
        pageBean.setTotal(findAll.getTotalElements());
        pageBean.setTotalPage(findAll.getTotalPages());
        return pageBean;
    }

    /**
     * 获取所有的电影列表
     *
     * @return
     */
    public List<Movie> findAll() {
        return movieDao.findAll();
    }

    /**
     * 获取排名前size的电影
     *
     * @param size
     * @return
     */
    public List<Movie> findTopList(Integer size) {
        Account account = (Account) SessionUtil.get(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY);
        if (account == null) return movieDao.findList(new Date(), size);
        try {
            return recommendService.recommendForUser(account, size);
        } catch (Exception e) {
            logger.error("recommendForUser failed, use default implementation.");
            e.printStackTrace();
            return movieDao.findList(new Date(), size);
        }
    }

    /**
     * 获取相关推荐
     *
     * @return
     * @params movie
     * @params size
     */
    public List<Movie> findRelatedList(Movie movie, Integer size) {
        try {
            return recommendService.recommendForMovie(movie, size);
        } catch (Exception e) {
            logger.error("recommendForMovie failed, use default implementation.");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 查找热门预告片
     *
     * @param size
     * @return
     */
    public List<Movie> findTopVideoList(Integer size) {
        return movieDao.findVideoList(size);
    }

    /**
     * 查找票房前五名
     *
     * @return
     */
    public List<Movie> findTopMoneyList() {
        return movieDao.findTop5ByOrderByTotalMoneyDesc();
    }

    /**
     * 获取正在上映的电影
     *
     * @return
     */
    public List<Movie> findShowList() {
        return movieDao.findShowList(new Date());
    }

    /**
     * 获取即将上映的电影
     *
     * @return
     */
    public List<Movie> findFutureList() {
        return movieDao.findFutureList(new Date());
    }

    /**
     * 返回总数
     *
     * @return
     */
    public long count() {
        return movieDao.count();
    }

    /**
     * 计算总票房
     *
     * @return
     */
    public BigDecimal sumTotalMoney() {
        return movieDao.sumTotalMoney();
    }
}
