package com.appcenter.study.common.event;

import com.appcenter.study.common.enums.BookingStatus;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookingCreatedEvent {
    private Long reservationId;
    private Long ticketId;
    private String username;
    private BookingStatus bookingStatus;
}
