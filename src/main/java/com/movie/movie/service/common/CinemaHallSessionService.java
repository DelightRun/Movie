package com.movie.movie.service.common;

import com.movie.movie.bean.PageBean;
import com.movie.movie.dao.common.CinemaHallSessionDao;
import com.movie.movie.entity.common.Cinema;
import com.movie.movie.entity.common.CinemaHallSession;
import com.movie.movie.entity.common.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 排片场次信息service层
 *
 * @author Administrator
 */
@Service
public class CinemaHallSessionService {

    @Autowired
    private CinemaHallSessionDao cinemaHallSessionDao;

    /**
     * 当cinemaHallSession的id不为空时进行编辑，当id为空时则进行添加
     *
     * @param cinemaHallSession
     * @return
     */
    public CinemaHallSession save(CinemaHallSession cinemaHallSession) {
        return cinemaHallSessionDao.save(cinemaHallSession);
    }


    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public CinemaHallSession findById(Long id) {
        return cinemaHallSessionDao.getOne(id);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(Long id) {
        cinemaHallSessionDao.deleteById(id);
    }

    /**
     * 分页查找排片场次列表信息
     *
     * @param cinemaHallSession
     * @param pageBean
     * @return
     */
    public PageBean<CinemaHallSession> findPage(CinemaHallSession cinemaHallSession, PageBean<CinemaHallSession> pageBean) {
        ExampleMatcher withMatcher = ExampleMatcher.matching().withMatcher("showDate", GenericPropertyMatchers.contains());
        //withMatcher = withMatcher.withIgnorePaths("status","sex");
        Example<CinemaHallSession> example = Example.of(cinemaHallSession, withMatcher);
        Pageable pageable = PageRequest.of(pageBean.getCurrentPage() - 1, pageBean.getPageSize());
        Page<CinemaHallSession> findAll = cinemaHallSessionDao.findAll(example, pageable);
        pageBean.setContent(findAll.getContent());
        pageBean.setTotal(findAll.getTotalElements());
        pageBean.setTotalPage(findAll.getTotalPages());
        return pageBean;
    }

    /**
     * 获取所有的场次列表
     *
     * @return
     */
    public List<CinemaHallSession> findAll() {
        return cinemaHallSessionDao.findAll();
    }

    /**
     * 统计电影上映场次
     *
     * @param movieId
     * @return
     */
    public List<Integer> getShowTotal(Long movieId) {
        return cinemaHallSessionDao.getShowTotal(movieId);
    }

    /**
     * 统计指定影院的放映场次
     *
     * @param cinemaId
     * @return
     */
    public List<Integer> getCinemaShowTotal(Long cinemaId) {
        return cinemaHallSessionDao.getCinemaShowTotal(cinemaId);
    }

    /**
     * 根据电影查询去重影院场次
     *
     * @param movieId
     * @return
     */
    public List<Cinema> findDistinctCinemaByMovieId(Long movieId) {
        return cinemaHallSessionDao.findDistinctCinemaList(movieId);
    }

    /**
     * 根据电影查询去重影院时间场次
     *
     * @param movieId
     * @return
     */
    public List<String> findDistinctShowDateByMovieId(Long movieId) {
        return cinemaHallSessionDao.findDistinctShowDateList(movieId);
    }

    /**
     * 根据影院id查询去重放映日期
     *
     * @param cinemaId
     * @return
     */
    public List<String> findDistinctShowDateListByCinemaId(Long cinemaId) {
        return cinemaHallSessionDao.findDistinctShowDateByCinemaList(cinemaId);
    }

    /**
     * 根据影院id，上映日期，查找去重的电影
     *
     * @param cinemaId
     * @param showDate
     * @return
     */
    public List<Movie> findDistinctMovieListByCinemaId(Long cinemaId, String showDate) {
        return cinemaHallSessionDao.findDistinctMovieByCinemaList(cinemaId, showDate);
    }

    /**
     * 获取指定电影、影院、时间段的场次
     *
     * @param movieId
     * @param cinemaId
     * @param showDate
     * @return
     */
    public List<CinemaHallSession> findByMovieIdAndCinemaIdAndShowDate(Long movieId, Long cinemaId, String showDate) {
        return cinemaHallSessionDao.findByMovieIdAndCinemaIdAndShowDate(movieId, cinemaId, showDate);
    }

    /**
     * 检查在指定的时间是否已经排片
     *
     * @param cinemaHallId
     * @param showDate
     * @param startTime
     * @return
     */
    public boolean isExistHall(Long id, Long cinemaHallId, String showDate, String startTime, String endTime) {
        return cinemaHallSessionDao.getHallCount(id == null ? 0l : id, cinemaHallId, showDate, startTime, endTime) > 0;
    }

    /**
     * 返回总数
     *
     * @return
     */
    public long count() {
        return cinemaHallSessionDao.count();
    }
}
