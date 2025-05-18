package com.amin.ameenserver.admin;

import com.amin.ameenserver.wallet.Transaction;
import com.amin.ameenserver.wallet.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminTransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping(value = "/transactions/{id}")
    public String getOne(@PathVariable Long id){
        model.addAttribute(transactionRepository.getById(id));
        return "admin/transaction";
    }

    @GetMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Transaction> getAll(@RequestParam(defaultValue = "1") int page){
        model.addAttribute("transactions", transactionRepository.findAll(Pageable.ofSize(25).withPage(page)).toList());
        return "admin/transactions";
    }

    @PostMapping("/transactions")
    public Transaction create(@RequestBody Transaction transaction){
        return transactionRepository.save(transaction);
    }

    @PostMapping("/transactions/{id}/update")
    public Transaction update(@PathVariable Long id, @RequestBody Transaction transaction){
        Transaction transaction2 = transactionRepository.getById(id);
        return transactionRepository.save(transaction);
        return "redirect:admin/transactions/" + id;
    }

    @PostMapping("/transactions/{id}/delete")
    public void delete(@PathVariable Long id){
        Transaction transaction = transactionRepository.getById(id);
        transactionRepository.delete(transaction);
        return "admin/transactions";
    }
}
