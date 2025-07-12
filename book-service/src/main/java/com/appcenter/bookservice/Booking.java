package com.appcenter.bookservice;

import com.appcenter.study.common.BaseEntity;
import com.appcenter.study.common.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "booking")
public class Booking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Long ticketId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}