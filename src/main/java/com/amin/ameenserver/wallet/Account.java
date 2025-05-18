package com.amin.ameenserver.wallet;

import com.amin.ameenserver.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by Al Ameen Omar on 08/07/2019.
 */

@Entity
@Getter
@Setter
@Table(name = "am_accounts")
public class Account implements Serializable {

    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "balance", nullable = false)
    private int balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountsType accountType;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "status", nullable = false)
    private String status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // @OneToMany(targetEntity = Transaction.class)
    // private List<Transaction> transactions;

    public Account() {
    }

    public Account(User user, String accountNumber, AccountsType accountType, int balance, boolean enabled, LocalDateTime createdAt) {
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.createdAt = createdAt;
        this.user = user;
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", accountType=" + accountType +
                ", enabled=" + enabled +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                ", user=" + user +
                ", status=" + status +
                '}';
    }
}
