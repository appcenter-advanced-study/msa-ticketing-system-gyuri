package com.appcenter.stockservice;

import com.appcenter.study.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "stock")
public class Stock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketStockId;

    private int quantity;

    private Long ticketId;

    // 티켓 수량 감소
    public void decreaseQuantity() throws RuntimeException {
        if (this.quantity <= 0) {
            throw new RuntimeException("티켓 잔여 수량 없음");
        }
        this.quantity--;
    }
}
