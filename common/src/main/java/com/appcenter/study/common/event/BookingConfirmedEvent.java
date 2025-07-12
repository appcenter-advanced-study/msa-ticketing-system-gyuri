package com.appcenter.study.common.event;

import com.appcenter.study.common.enums.BookingStatus;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookingConfirmedEvent {
    private Long reservationId;
    private BookingStatus bookingStatus;
}
