package com.movie.movie.schedule.admin;

import com.movie.movie.entity.common.Order;
import com.movie.movie.entity.common.PayLog;
import com.movie.movie.service.common.OrderService;
import com.movie.movie.service.common.PayLogService;
import com.movie.movie.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;

/**
 * 订单状态检测定时器
 *
 * @author Administrator
 */
@Configuration
@EnableScheduling
public class PayStatusSchedule {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayLogService payLogService;

    @Value("${ylrc.order.timeout}")
    private Integer orderTimeout;//订单过期时间

    private final Logger log = LoggerFactory.getLogger(PayStatusSchedule.class);

    @Scheduled(fixedDelay = 10000)//每隔10秒检查一次是否超时
    public void checkOrder() {
        log.info("开始执行订单状态检查任务！");
        Date outTime = StringUtil.getBeforeDate(new Date(), orderTimeout / 60);
        List<Order> orders = orderService.findTimeOutList(outTime);
        for (Order order : orders) {
            orderService.cancelOrder(order.getSn());
        }
        log.info("成功将【" + StringUtil.getFormatterDate(outTime, "yyyy-MM-dd HH:mm:ss") + "】时间内超时的【" + orders.size() + "】个订单自动取消!");
    }

    @Scheduled(fixedDelay = 10000)
    public void checkPayLog() {
        log.info("开始执行充值订单状态检查任务！");
        Date outTime = StringUtil.getBeforeDate(new Date(), orderTimeout / 60);
        List<PayLog> payLogs = payLogService.findAllPayTimeout(outTime);
        for (PayLog payLog : payLogs) {
            payLogService.cancelOrder(payLog);
        }
        log.info("成功将【" + StringUtil.getFormatterDate(outTime, "yyyy-MM-dd HH:mm:ss") + "】时间内超时的【" + payLogs.size() + "】个充值订单自动取消!");
    }
}
