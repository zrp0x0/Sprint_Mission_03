package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class BaseEntity {

    protected UUID id;
    protected Long createAt;
    protected Long updateAt;

    protected BaseEntity() {
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis(); // ms 단위
        this.updateAt = createAt;
    }

    protected void touch() {
        this.updateAt = System.currentTimeMillis();
    }
}
