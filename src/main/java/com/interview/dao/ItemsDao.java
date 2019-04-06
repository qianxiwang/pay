package com.interview.dao;

import com.interview.domain.Items;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsDao extends JpaRepository<Items, String> {
}
