package com.interview.service;

import com.interview.dao.ItemsDao;
import com.interview.domain.Items;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ItemsService {

    @Autowired
    private ItemsDao itemsDao;


    public Items getItem(String itemId) {
        return itemsDao.getOne(itemId);
    }

    public void save(Items items) {
        items.setId(UUID.randomUUID().toString());
        itemsDao.save(items);
    }

    public int getItemCount(String itemId) {
        return itemsDao.getOne(itemId).getCounts();
    }


}
