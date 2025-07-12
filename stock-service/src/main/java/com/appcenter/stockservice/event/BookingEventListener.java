package com.appcenter.stockservice.event;


import com.appcenter.stockservice.Stock;
import com.appcenter.stockservice.StockRepository;
import com.appcenter.study.common.EventUtil;
import com.appcenter.study.common.enums.BookingStatus;
import com.appcenter.study.common.event.BookingCancelEvent;
import com.appcenter.study.common.event.BookingConfirmedEvent;
import com.appcenter.study.common.event.BookingCreatedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventListener {
    private final StockRepository stockRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(topics = "reservation.created", groupId = "booking-created-group")
    @Transactional
    public void handle(String message) {
        log.info("message :{}",message);
        BookingCreatedEvent event = EventUtil.mappingMessageToClass(message, BookingCreatedEvent.class);
        log.info("[Kafka] 예약 생성 이벤트 수신 : {}", event);

        if (event != null) {
            // 1. 티켓 재고 조회
            Stock stock = stockRepository.findByTicketIdForUpdate(event.getTicketId())
            .orElseThrow(() -> new RuntimeException("티켓 재고 없음"));

            // 2. 티켓 재고 0 이하 여부 확인
            if (stock.getQuantity() <= 0) {
                kafkaTemplate.send("reservation.cancel", new BookingCancelEvent(
                        event.getReservationId(), BookingStatus.CANCELLED
                ));
                log.info("[Kafka] 재고 없으므로 예약 취소 : {}", event.getReservationId());
                return;
            }

            // 3. 티켓 재고 차감
            stock.decreaseQuantity();
            stockRepository.save(stock);

            log.info("[StockService] ➖ 티켓 재고 차감 완료: ticketId={}, 남은 수량={}", event.getTicketId(), stock.getQuantity());

            // 4. Kafka 예약 확인 이벤트 발행
            BookingConfirmedEvent confirmedEvent = BookingConfirmedEvent.builder()
                    .reservationId(event.getReservationId())
                    .bookingStatus(BookingStatus.CONFIRMED) // 예약 대기
                    .build();

            kafkaTemplate.send("reservation.confirmed", confirmedEvent);

            log.info("[Kafka] 예약 확인 이벤트 발행 : {}", event.getReservationId());
        }
    }
}
