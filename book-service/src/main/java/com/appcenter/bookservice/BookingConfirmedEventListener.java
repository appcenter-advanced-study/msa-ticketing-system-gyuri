package com.appcenter.bookservice;

import com.appcenter.study.common.EventUtil;
import com.appcenter.study.common.enums.BookingStatus;
import com.appcenter.study.common.event.BookingCancelEvent;
import com.appcenter.study.common.event.BookingConfirmedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingConfirmedEventListener {
    private final BookingRepository bookingRepository;

    @KafkaListener(topics = "reservation.confirmed", groupId = "booking-confirmed-group")
    @Transactional
    public void handleConfirmed(String message) {
        BookingConfirmedEvent event = EventUtil.mappingMessageToClass(message, BookingConfirmedEvent.class);
        log.info("[Kafka] 예약 확인 이벤트 수신 : {}", event);

        if (event == null) return;

        // 1. 예약 조회
        Booking booking = bookingRepository.findById(event.getReservationId())
                .orElseThrow(() -> new RuntimeException("해당 예약 ID 없음: " + event.getReservationId()));

        // 2. 예약 확인 저장
        Booking update = Booking.builder()
                                .bookingId(booking.getBookingId())
                                .username(booking.getUsername())
                                .ticketId(booking.getTicketId())
                                .status(BookingStatus.CONFIRMED)
                                .build();
        bookingRepository.save(update);
        log.info("예약 확인 저장");
    }
}
