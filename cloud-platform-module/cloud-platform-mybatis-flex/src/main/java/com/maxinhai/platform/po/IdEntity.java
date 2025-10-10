package com.maxinhai.platform.po;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;

public abstract class IdEntity {

    @Id(keyType = KeyType.Auto)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
