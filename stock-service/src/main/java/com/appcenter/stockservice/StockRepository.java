package com.appcenter.stockservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    //    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ts FROM Stock ts WHERE ts.ticketId = :ticketId")
    Optional<Stock> findByTicketIdForUpdate(@Param("ticketId") Long ticketId);
}
