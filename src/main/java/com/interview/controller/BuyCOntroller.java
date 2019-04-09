package com.interview.controller;

import com.interview.service.PayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BuyCOntroller {

    @Autowired
    private PayService payService;

    /**
     * http://localhost:9999/pay/buy?itemId=1
     * @param itemId
     * @return
     */

    @GetMapping("/buy")
    @ResponseBody
    public String buy(String itemId) {

        if (StringUtils.isNotBlank(itemId)) {
            if (payService.buy(itemId)) {
                return "订单创建成功";
            } else {
                return "订单创建失败";
            }
        } else {

            return "条目ID不能为空";
        }
    }


    @GetMapping("/buy2")
    @ResponseBody
    public String buy2(String itemId) {

        if (StringUtils.isNotBlank(itemId)) {
            if (payService.buy(itemId)) {
                return "订单创建成功";
            } else {
                return "订单创建失败";
            }
        } else {

            return "条目ID不能为空";
        }
    }
}
