package com.movie.movie.service.common;

import com.movie.movie.bean.PageBean;
import com.movie.movie.dao.common.AccountDao;
import com.movie.movie.entity.common.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.stereotype.Service;

/**
 * 前端用户信息service层
 *
 * @author Administrator
 */
@Service
public class AccountService {

    @Autowired
    private AccountDao accountDao;

    /**
     * 当account的id不为空时进行编辑，当id为空时则进行添加
     *
     * @param account
     * @return
     */
    public Account save(Account account) {
        return accountDao.save(account);
    }

    /**
     * 根据手机号查找
     *
     * @param mobile
     * @return
     */
    public Account find(String mobile) {
        return accountDao.findByMobile(mobile);
    }


    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public Account findById(Long id) {
        return accountDao.find(id);
    }

    /**
     * 根据id删除
     *
     * @param id
     */
    public void delete(Long id) {
        accountDao.deleteById(id);
    }

    /**
     * 分页查找用户信息
     *
     * @param account
     * @param pageBean
     * @return
     */
    public PageBean<Account> findPage(Account account, PageBean<Account> pageBean) {
        ExampleMatcher withMatcher = ExampleMatcher.matching().withMatcher("mobile", GenericPropertyMatchers.contains());
        withMatcher = withMatcher.withIgnorePaths("balance", "sex", "status");
        Example<Account> example = Example.of(account, withMatcher);
        Pageable pageable = PageRequest.of(pageBean.getCurrentPage() - 1, pageBean.getPageSize());
        Page<Account> findAll = accountDao.findAll(example, pageable);
        pageBean.setContent(findAll.getContent());
        pageBean.setTotal(findAll.getTotalElements());
        pageBean.setTotalPage(findAll.getTotalPages());
        return pageBean;
    }

    /**
     * 返回总数
     *
     * @return
     */
    public long count() {
        return accountDao.count();
    }

    public int rs(int sex) {
        return accountDao.findsex(sex).size();
    }
}
