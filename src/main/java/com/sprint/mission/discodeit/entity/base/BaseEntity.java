package com.sprint.mission.discodeit.entity.base;

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

    protected BaseEntity(BaseEntity other) {
        this.id = other.id;
        this.createAt = other.createAt;
        this.updateAt = other.updateAt;
    }

    protected void touch() {
        this.updateAt = System.currentTimeMillis();
    }

    public abstract BaseEntity copy();
}
