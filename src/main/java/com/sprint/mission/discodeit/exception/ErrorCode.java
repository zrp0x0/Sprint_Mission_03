package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // File IO Error
    FILE_IO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 읽기/쓰기 중 오류가 발생했습니다."),
    FILE_DATA_CORRUPTED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 데이터가 손상되었거나 형식이 맞지 않습니다."),

    //
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채널을 찾을 수 없습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
