package com.appcenter.bookservice;

import com.appcenter.study.common.enums.BookingStatus;
import com.appcenter.study.common.EventUtil;
import com.appcenter.study.common.event.BookingCancelEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingCancelEventListener {
    private final BookingRepository bookingRepository;

    @KafkaListener(topics = "reservation.cancel", groupId = "booking-cancel-group")
    @Transactional
    public void handleCancel(String message) {
        BookingCancelEvent event = EventUtil.mappingMessageToClass(message, BookingCancelEvent.class);
        log.info("[Kafka] 예약 취소 이벤트 수신 : {}", event);

        if (event == null) return;

        // 1. 예약 조회
        Booking booking = bookingRepository.findById(event.getReservationId())
                .orElseThrow(() -> new RuntimeException("해당 예약 ID 없음: " + event.getReservationId()));

        // 2. 예약 취소 저장
        Booking update = Booking.builder()
                                .bookingId(booking.getBookingId())
                                .username(booking.getUsername())
                                .ticketId(booking.getTicketId())
                                .status(BookingStatus.CANCELLED)
                                .build();
        bookingRepository.save(update);
        log.info("예약 취소 저장");
    }
}
