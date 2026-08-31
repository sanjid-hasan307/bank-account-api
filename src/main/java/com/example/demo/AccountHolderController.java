package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountHolderController {

    @Autowired
    private AccountHolderService service;

    @PostMapping
    public AccountHolder create(@RequestBody AccountHolder accountHolder) {
        return service.createAccount(accountHolder);
    }

    @GetMapping
    public List<AccountHolder> getAll() {
        return service.getAllAccounts();
    }

    @GetMapping("/{id}")
    public AccountHolder getOne(@PathVariable Long id) {
        return service.getAccountById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteAccount(id);
    }
}