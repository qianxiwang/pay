package com.interview;

import com.interview.service.PayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PayApplicationTests {

    @Autowired
    private PayService payService;

    @Test
    @Transactional
    public void testBuy(){

        payService.buy("1");

    }

}
