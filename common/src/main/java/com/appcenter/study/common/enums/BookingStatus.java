package com.appcenter.study.common.enums;

public enum BookingStatus {
    PENDING,     // 예약 직후, 재고 차감 대기 중
    CONFIRMED,   // 재고 차감 완료
    CANCELED    // 재고 부족 등으로 예약 취소됨
}
