package com.amin.ameenserver.wallet;

import com.amin.ameenserver.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "from_account", nullable = false)
    private Long fromAccount;

    @Column(name = "to_account", nullable = false)
    private Long toAccount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "current_balance", nullable = false)
    private Integer currentBalance;

}
