package com.appcenter.stockservice.event;

import com.appcenter.stockservice.Stock;
import com.appcenter.stockservice.StockRepository;
import com.appcenter.study.common.EventUtil;
import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventListener {
    private final StockRepository stockRepository;

    @KafkaListener(topics = "reservation.created", groupId = "stock-group")
    @Transactional
    public void handle(String message) {
        BookingCreatedEvent event = EventUtil.mappingMessageToClass(message, BookingCreatedEvent.class);
        log.info("[Kafka] 예약 생성 이벤트 수신 : {}", event);

        if (event != null) {
            Stock stock = stockRepository.findByTicketIdForUpdate(event.getTicketId())
                    .orElseThrow(() -> new RuntimeException("티켓 재고 없음"));

            if (stock.getQuantity() <= 0) {
                throw new RuntimeException("재고 부족");
            }

            stock.decreaseQuantity();
            stockRepository.save(stock);

            log.info("[StockService] ➖ 티켓 재고 차감 완료: ticketId={}, 남은 수량={}", event.getTicketId(), stock.getQuantity());
        }
    }
}
