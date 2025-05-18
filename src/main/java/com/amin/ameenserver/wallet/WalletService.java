package com.amin.ameenserver.wallet;

import com.amin.ameenserver.core.ResourceNotFoundException;
import com.amin.ameenserver.order.Order;
import com.amin.ameenserver.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class WalletService {

    private final CreditAccountRepository creditAccountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public WalletService(CreditAccountRepository creditAccountRepository, TransactionRepository transactionRepository) {
        this.creditAccountRepository = creditAccountRepository;
        this.transactionRepository = transactionRepository;
    }


    public Account createAccount(User user, Account account){
        account.setUser(user);
        return creditAccountRepository.save(account);
    }

    public Account createAccount(User user, AccountsType accountsType){
        String accNum = UUID.randomUUID().toString().replace("-", "");
        Account account = new Account(user, accNum, accountsType, 0, true, new Date());
        account.setUser(user);
        return creditAccountRepository.save(account);
    }

    public Account getUserAccount(User user){
        return creditAccountRepository.findById(user.getId()).orElse(null);
    }

    public Account getUserAccount(String accNum){
        return creditAccountRepository.findByAccountNumber(accNum)
                .orElseThrow(() -> new ResourceNotFoundException(""));
    }

    public Page<Account> getAllTransitions(int page, int count){
        Pageable pageable = Pageable.ofSize(count).withPage(page);
        return creditAccountRepository.findAll(pageable);
    }

    public void getUserTrans(User user, int page){}

    public Transaction getTransactionById(long id){
        return transactionRepository.findById(id).orElse(null);
    }

    public Transaction createTransaction(Integer amount, Long fromAccountId, Long toAccountId, TransactionType transactionType, String description, Order order) {

    Account toAccount = creditAccountRepository.findById(toAccountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + toAccountId));
    Account fromAccount = creditAccountRepository.findById(fromAccountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + fromAccountId));

    toAccount.setBalance(toAccount.getBalance() + amount);
    creditAccountRepository.save(toAccount);

    fromAccount.setBalance(fromAccount.getBalance() - amount);
    creditAccountRepository.save(fromAccount);

    Transaction creditTransaction = new Transaction();
    creditTransaction.setType(transactionType);
    creditTransaction.setDescription(description);
    creditTransaction.setCreatedAt(new Date());
    creditTransaction.setToAccount(toAccountId);
    creditTransaction.setFromAccount(fromAccountId);
    creditTransaction.setAmount(amount);
    creditTransaction.setOrder(order);

    Transaction debitTransaction = new Transaction();
    debitTransaction.setType(transactionType);
    debitTransaction.setDescription(description);
    debitTransaction.setCreatedAt(new Date());
    debitTransaction.setToAccount(toAccountId);
    debitTransaction.setFromAccount(fromAccountId);
    debitTransaction.setAmount(amount * (-1));
    debitTransaction.setOrder(order);

    transactionRepository.save(debitTransaction);

    return transactionRepository.save(creditTransaction);
    }

}
