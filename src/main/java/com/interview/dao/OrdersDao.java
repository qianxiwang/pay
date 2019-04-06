package com.interview.dao;

import com.interview.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersDao extends JpaRepository<Orders,String> {
}
