package com.amin.ameenserver.admin;

import com.amin.ameenserver.wallet.Account;
import com.amin.ameenserver.wallet.CreditAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminCreditAccountController {

    @Autowired
    private CreditAccountRepository accountRepository;

    public void create(){}

    @GetMapping("/accounts/{id}")
    public Account getOne(@PathVariable long id){
        return accountRepository.getById(id);
    }

    @GetMapping("/accounts")
    public List<Account> getAll(){
        return accountRepository.findAll();
    }

    @PostMapping("/accounts/{id}/update")
    public void update(@PathVariable long id){}

    @PostMapping("/accounts/{id}/delete")
    public void delete(@PathVariable long id){}
}
