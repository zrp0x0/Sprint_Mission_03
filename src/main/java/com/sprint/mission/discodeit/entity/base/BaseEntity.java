package com.sprint.mission.discodeit.entity.base;

import lombok.Getter;

import java.time.Instant;

@Getter // equals and hash도 해야될 듯? - 공부하고 하자
public abstract class BaseEntity extends ImmutableBaseEntity {

    protected Instant updateAt;

    protected BaseEntity() {
        super();
        this.updateAt = createAt;
    }

    protected BaseEntity(BaseEntity other) {
        super(other);
        this.updateAt = other.updateAt;
    }

    protected void touch() {
        this.updateAt = Instant.now();
    }

    public abstract BaseEntity copy();
}
