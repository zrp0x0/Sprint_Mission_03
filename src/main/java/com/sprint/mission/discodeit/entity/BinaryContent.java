package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.base.ImmutableBaseEntity;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public class BinaryContent extends ImmutableBaseEntity {

    private String fileName;
    private String contentType;
    private byte[] data; // 깊은 복사 주의

    private BinaryContent(String fileName, String contentType, byte[] data) {
        super();
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data.clone();
    }

    private BinaryContent(BinaryContent other) {
        super(other);
        this.fileName = other.fileName;
        this.contentType = other.contentType;
        this.data = other.data.clone();
    }

    public BinaryContent copy() {
        return new BinaryContent(this);
    }

    public static BinaryContent create(String fileName, String contentType, byte[] data) {
        return new BinaryContent(fileName, contentType, data); // 주의 깊은 복사
    }
}

// data[] null 처리를 해야하는데
// - 이게 DTO에서 처리하는걸로 만족
// - 내부 로직에서 처리까지 하는 걸로?
// - ㅋㅋ 이거 개빡세다. 내부 로직까지 처리하면 완전 철저히 안전
// - 근데 이거의 범위는 어느정도 전체 시스템의 합의가 있어야 이루어질 수 있을 것 같은데? 질문