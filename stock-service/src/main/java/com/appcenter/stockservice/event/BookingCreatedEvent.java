package com.appcenter.stockservice.event;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookingCreatedEvent {
    private Long reservationId;
    private Long ticketId;
    private String username;
}
