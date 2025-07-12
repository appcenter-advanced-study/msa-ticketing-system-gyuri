package com.appcenter.study.common.response.resEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    // COMMON
    FOUND_NO_SEARCH_RESULT(200, "조회결과가 없습니다."),
    FOUND_IT(201, "조회가 완료되었습니다."),
    FOUND_LIST(201, "목록 조회가 완료되었습니다."),
    NO_CONTENT(200, "검색된 결과가 없습니다."),

    // BOOKING
    CREATE_BOOKING(201, "예매 요청이 정상적으로 완료되었습니다.")
    ;

    private final Integer code;
    private final String message;
}
