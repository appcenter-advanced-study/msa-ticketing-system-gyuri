package com.appcenter.bookservice.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long bookingId;
    private String username;
    private Long ticketId;
}
