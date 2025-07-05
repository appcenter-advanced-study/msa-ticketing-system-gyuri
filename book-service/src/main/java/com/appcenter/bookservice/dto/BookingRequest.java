package com.appcenter.bookservice.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BookingRequest {
    private Long ticketId;
    private String userName;
}
