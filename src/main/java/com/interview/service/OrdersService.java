package com.interview.service;

import com.interview.dao.OrdersDao;
import com.interview.domain.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class OrdersService {

    @Autowired
    private OrdersDao ordersDao;

    public boolean save(String itemId) {

        try {
            Orders o = new Orders();
            o.setId(UUID.randomUUID().toString());
            o.setItemId(itemId);

            ordersDao.save(o);

            log.info("订单创建成功.......");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

}
