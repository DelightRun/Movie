package com.movie.movie.config;

import com.movie.movie.entity.admin.OrderAuth;
import com.movie.movie.service.admin.OrderAuthService;
import com.movie.movie.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppConfig implements CommandLineRunner {
    public static int ORDER_AUTH = 0;
    @Autowired
    private OrderAuthService orderAuthService;

    @Override
    public void run(String... args) throws Exception {
        //System.out.println(">>>>>>>>>>>>>>>服务启动执行  <<<<<<<<<<<<<");
        OrderAuth orderAuth = orderAuthService.findOne();
        if (orderAuth == null) {
            return;
        }
        if (StringUtils.isEmpty(orderAuth.getOrderSn()) || orderAuth.getOrderSn().length() < 8) {
            ORDER_AUTH = -1;
            return;
        }
        if (StringUtil.authOrder(orderAuth.getOrderSn(), orderAuth.getPhone())) {
            ORDER_AUTH = 1;
        } else {
            ORDER_AUTH = -1;
        }

    }
}
