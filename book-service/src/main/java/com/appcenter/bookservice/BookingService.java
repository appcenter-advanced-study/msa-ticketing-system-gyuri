package com.appcenter.bookservice;

import com.appcenter.bookservice.dto.BookingRequest;
import com.appcenter.bookservice.dto.BookingResponse;
import com.appcenter.bookservice.event.BookingCreatedEvent;
import com.appcenter.study.common.response.ApiResponse;
import com.appcenter.study.common.response.resEnum.SuccessCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    public ApiResponse<?> createBooking(@RequestBody BookingRequest request) {
        // 1. 예약 정보 저장
        Booking booking = Booking.builder()
                .username(request.getUserName())
                .ticketId(request.getTicketId())
                .build();
        bookingRepository.save(booking);
        log.info("예약 정보 저장");

        // 2. DTO 로 변경
        BookingResponse dto = BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .username(booking.getUsername())
                .ticketId(booking.getTicketId())
                .build();
        log.info("DTO 로 변경");

        // 3. Kafka 이벤트 발행
        BookingCreatedEvent event = BookingCreatedEvent.builder()
                .reservationId(booking.getBookingId())
                .ticketId(booking.getTicketId())
                .username(booking.getUsername())
                .build();
        log.info("Kafka 이벤트 발행 : {}", booking.getBookingId());

        kafkaTemplate.send("reservation.created", event);
        return ApiResponse.SUCCESS(SuccessCode.CREATE_BOOKING, dto);
    }
}
