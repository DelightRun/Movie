package com.movie.movie.controller.admin;

import com.movie.movie.bean.PageBean;
import com.movie.movie.bean.Result;
import com.movie.movie.entity.common.Account;
import com.movie.movie.service.common.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户管理控制器
 *
 * @author Administrator
 */
@RequestMapping("/admin/account")
@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 用户列表
     *
     * @param model
     * @param account
     * @param pageBean
     * @return
     */
    @RequestMapping(value = "/list")
    public String list(Model model, Account account, PageBean<Account> pageBean) {
        model.addAttribute("pageBean", accountService.findPage(account, pageBean));
        model.addAttribute("mobile", account.getMobile());
        return "admin/account/list";
    }

    /**
     * 编辑用户状态
     *
     * @param account
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> edit(Account account) {
        Account findById = accountService.findById(account.getId());
        findById.setStatus(account.getStatus());
        //表示数据合法，可以保存到数据库
        accountService.save(findById);
        return Result.success(true);
    }
}
