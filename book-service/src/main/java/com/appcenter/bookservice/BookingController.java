package com.appcenter.bookservice;

import com.appcenter.bookservice.dto.BookingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping("/purchase")
    public ResponseEntity<?> purchaseTicket(@RequestBody BookingRequest request) {
        log.info("예매 요청 전송");
        return ResponseEntity.ok(bookingService.createBooking(request));
    }
}
