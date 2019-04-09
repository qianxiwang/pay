package com.interview.service;

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


    public boolean buy(String itemId) {

        //每次购买9个
        int buyCount = 9;

        //判断库存
        int count = itemsService.getItemCount(itemId);

        if (count < buyCount) {
            log.error("库存不足，下单失败");
            return false;
        }

        return true;
    }
}
