package com.amin.ameenserver.wallet;

import com.amin.ameenserver.user.User;
import com.amin.ameenserver.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class WalletController {

    private final UserService userService;
    private final WalletService walletService;

    @Autowired
    public WalletController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

@GetMapping("/api/v1/transactions")
public ResponseEntity<List<Transaction>> getAllTransactions(Authentication authentication, @RequestParam(defaultValue = "1") int page) {
    if (authentication == null || !authentication.isAuthenticated()) {
        throw new UsernameNotFoundException("User not found");
    }

    String username = authentication.getName();
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    
    List<Transaction> transactions = transactionRepository.findByFromAccountOrToAccount(user.getId(), user.getId());
    
    return ResponseEntity.ok(transactions);
}


    @GetMapping("/api/v1/transactions/{id}")
public ResponseEntity<Transaction> getTransaction(@PathVariable long id) {
    Optional<Transaction> transaction = transactionRepository.findById(id);
    
    if (transaction.isPresent()) {
        return ResponseEntity.ok(transaction.get());
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}

    

@PostMapping("/api/v1/transactions")
public ResponseEntity<String> createTransaction(@RequestBody Transaction newTransaction) {
    try {
        transactionRepository.save(newTransaction);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction created successfully");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating transaction");
    }
}


@PostMapping("/api/v1/transactions/{id}/delete")
public ResponseEntity<String> deleteTransaction(@PathVariable long id) {
    Optional<Transaction> transaction = transactionRepository.findById(id);
    
    if (transaction.isPresent()) {
        transactionRepository.deleteById(id);
        return ResponseEntity.ok("Transaction deleted successfully");
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found");
    }
}


    
    @PostMapping("/api/v1/transactions/{id}/update")
    public ResponseEntity<String> updateTransaction(@PathVariable long id, @RequestBody Transaction updatedTransaction) {
        Optional<Transaction> existingTransaction = transactionRepository.findById(id);
    
        if (existingTransaction.isPresent()) {
            Transaction transaction = existingTransaction.get();
            // Update the transaction details here
            transaction.setAmount(updatedTransaction.getAmount());
            transaction.setDate(updatedTransaction.getDate());
            // Add other fields as necessary

            transactionRepository.save(transaction);
            return ResponseEntity.ok("Transaction updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found");
        }
    }


    @GetMapping("/admin/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(required = false) Long uid,
        @RequestParam(defaultValue = "") String sort) {

       User user = userService.findUserById(uid)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    
       List<Transaction> transactions = transactionRepository.findByFromAccountOrToAccount(user.getId(), user.getId());
    
       return ResponseEntity.ok(transactions);
   }

    @GetMapping("/admin/transactions/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable long id){
        var trans = transactionService.findById(id);
        return new ResponseEntity<>(trans, HttpStatus.OK);
    }

    @PostMapping("/admin/transactions")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction){

        var trans = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(trans, HttpStatus.OK);
    }

    @PostMapping("/admin/transactions/{id}/delete")
    public ResponseEntity<Transaction> deleteTransaction(@PathVariable long id){

        var trans = transactionService.deleteTransaction(id);
        return new ResponseEntity<>(trans, HttpStatus.OK);
    }

    @PostMapping("/admin/transactions/{id}/update")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable long id, @RequestBody Transaction transaction){

        var trans = transactionService.updateTransaction(id, transaction);
        return new ResponseEntity<>(trans, HttpStatus.OK);
    }
}
