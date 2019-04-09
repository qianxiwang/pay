package com.interview.service;

import com.interview.utils.Lock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PayService {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private Lock lock;

    public boolean buy2(String itemId) {

        lock.getLock();

        //每次购买个
        int buyCount = 5;

        //判断库存
        int count = itemsService.getItemCount(itemId);

        log.info("数量：~~~~~~~~~~~~~~" + count);

        if (count < buyCount) {
            log.error("库存不足，下单失败，购买数{}件，库存只有{}件", buyCount, count);

            lock.releaseLock();
            return false;
        }

        //创建订单
        boolean flag = ordersService.save(itemId);

        //模拟高并发场景
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            lock.releaseLock();
            e.printStackTrace();
        }

        //扣库存
        if (flag) {
            itemsService.reduceCount(itemId, buyCount);
            lock.releaseLock();

            int reducecount = itemsService.getItemCount(itemId);
            log.info("剩余数量：~~~~~~~~~~~~~~" + reducecount);

        } else {
            log.error("订单创建失败......");
            lock.releaseLock();
            return false;
        }

        return true;
    }

    public boolean buy(String itemId) {

        //每次购买个
        int buyCount = 5;

        //判断库存
        int count = itemsService.getItemCount(itemId);

        log.info("数量：~~~~~~~~~~~~~~" + count);

        if (count < buyCount) {
            log.error("库存不足，下单失败，购买数{}件，库存只有{}件", buyCount, count);
            return false;
        }

        //创建订单
        boolean flag = ordersService.save(itemId);

        //模拟高并发场景
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //扣库存
        if (flag) {
            itemsService.reduceCount(itemId, buyCount);

            int reducecount = itemsService.getItemCount(itemId);
            log.info("剩余数量：~~~~~~~~~~~~~~" + reducecount);


        } else {
            log.error("订单创建失败......");
            return false;
        }

        return true;
    }
}
