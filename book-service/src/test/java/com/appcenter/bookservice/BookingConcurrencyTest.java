package com.appcenter.bookservice;


import com.appcenter.bookservice.dto.BookingRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class BookingConcurrencyTest {

    @Autowired
    private BookingService bookingService;

    private static final int THREAD_COUNT = 100;

    @Test
    void 동시_예약요청_100건_시뮬레이션() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int userIndex = i;
            executor.submit(() -> {
                try {
                    BookingRequest request = BookingRequest.builder()
                            .userName("user" + userIndex)
                            .ticketId(1L) // 테스트할 ticketId
                            .build();
                    bookingService.createBooking(request);
                } catch (Exception e) {
                    System.out.println("예약 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 요청 완료까지 대기
        System.out.println("✅ 예약 요청 100건 시뮬레이션 완료");
    }
}

