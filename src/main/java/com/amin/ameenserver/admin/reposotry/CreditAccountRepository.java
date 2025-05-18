package com.amin.ameenserver.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CreditAccountRepository extends JpaRepository<Account, Long> {
    @Query(value = "SELECT id from Account a where account_type='SYSTEM' limit 1", nativeQuery = true)
    Long findFeeAccountId();

    Optional<Account> findByAccountNumber(String accNum);
}