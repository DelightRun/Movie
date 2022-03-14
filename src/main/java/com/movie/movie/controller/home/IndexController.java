package com.movie.movie.controller.home;

import com.movie.movie.bean.CodeMsg;
import com.movie.movie.bean.Result;
import com.movie.movie.constant.SessionConstant;
import com.movie.movie.entity.common.Account;
import com.movie.movie.entity.common.Area;
import com.movie.movie.service.common.*;
import com.movie.movie.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 前台首页
 *
 * @author Administrator
 */
@RequestMapping("/home/index")
@Controller
public class IndexController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AreaService areaService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private NewsService newsService;
    @Autowired
    private AccountService accountService;

    /**
     * 前台首页
     *
     * @param model
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("topMovieList", movieService.findTopList(12));
        model.addAttribute("topCinemaList", cinemaService.findTopList());
        model.addAttribute("topVideoMovieList", movieService.findTopVideoList(4));
        model.addAttribute("topMoneyMovieList", movieService.findTopMoneyList());
        model.addAttribute("topNewsList", newsService.findTop());
        return "home/index/index";
    }

    /**
     * 注册页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        Account account = (Account) SessionUtil.get(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY);
        if (account != null) {
            return "redirect:/home/account/user-center";
        }
        return "home/index/register";
    }

    /**
     * 退出登录
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(Model model) {
        SessionUtil.set(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY, null);
        return "redirect:index";
    }

    /**
     * 改变地域
     *
     * @param cid
     * @return
     */
    @RequestMapping(value = "/change_area", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> changeArea(@RequestParam(name = "cid", required = true) Long cid) {
        Area area = areaService.findById(cid);
        if (area == null) {
            return Result.error(CodeMsg.HOME_AREA_NO_EXIST);
        }
        SessionUtil.set(SessionConstant.SESSION_HOME_AREA, area);
        return Result.success(true);
    }

    /**
     * 注册表单提交
     *
     * @param mobile
     * @param password
     * @return
     */
    @GetMapping(value = "/toregister")

    public String register(String mobile, String password) {

        //检查手机号是否已经注册
        Account account = new Account();
        account.setHeadPic("default-head-pic.jpg");
        account.setMobile(mobile);
        account.setPassword(password);

        accountService.save(account);
        return "redirect:index";
    }

    /**
     * 用户登录
     *
     * @param mobile
     * @param password
     * @param checkCode
     * @param tag
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> login(@RequestParam(name = "mobile", required = true) String mobile,
                                 @RequestParam(name = "password") String password,
                                 @RequestParam(name = "checkCode") String checkCode,
                                 @RequestParam(name = "tag") Integer tag
    ) {
        Account account = null;
        if (tag == 1) {
            //表示是密码登录
            if (StringUtils.isEmpty(password)) {
                return Result.error(CodeMsg.ADMIN_PASSWORD_EMPTY);
            }
            account = accountService.find(mobile);
            if (account == null) {
                return Result.error(CodeMsg.HOME_ACCOUNT_REGISTER_MOBILE_NO_EXIST);
            }
            //表示一切符合，开始检查密码是否正确
            if (!password.equals(account.getPassword())) {
                return Result.error(CodeMsg.HOME_ACCOUNT_LOGIN_PWD_ERROR);
            }
        }
        if (tag == 2) {
            //从session中获取短信验证码
            String smsCode = (String) SessionUtil.get("home_login_sms_code");
            if (smsCode == null) {
                return Result.error(CodeMsg.SESSION_EXPIRED);
            }
            if (!smsCode.equalsIgnoreCase(checkCode)) {
                return Result.error(CodeMsg.SMS_CODE_ERROR);
            }
            //短信验证码正确
            SessionUtil.set("home_login_sms_code", null);
            //检查手机号是否已经注册
            account = accountService.find(mobile);
            if (account == null) {
                return Result.error(CodeMsg.HOME_ACCOUNT_REGISTER_MOBILE_NO_EXIST);
            }
        }
        //检查用户状态是否正常
        if (account.getStatus() != Account.account_status_able) {
            return Result.error(CodeMsg.HOME_ACCOUNT_STATUS_ERROR);
        }
        SessionUtil.set(SessionConstant.SESSION_ACCOUNT_LOGIN_KEY, account);
        return Result.success(true);
    }
}
