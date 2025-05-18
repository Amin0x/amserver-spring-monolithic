package com.amin.ameenserver.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    @Query("select b from Bid b where user_id = ?1 and order_id = ?2")
    Optional<Bid> findByUserAndOrder(Long userId, Long orderId);
}