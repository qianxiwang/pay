package com.interview.domain;

import javax.persistence.*;

@Entity
public class Orders {

    @Id
    private String id;
    private String itemId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
