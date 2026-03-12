package com.sprint.mission.discodeit.entity.base;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter // equals and hash도 해야될 듯? - 공부하고 하자
public abstract class BaseEntity implements Serializable {

    private static final long seraiVersionUID = 1L;

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
