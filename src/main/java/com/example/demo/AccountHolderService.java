package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountHolderService {

    @Autowired
    private AccountHolderRepository repository;

    public AccountHolder createAccount(AccountHolder accountHolder) {
        return repository.save(accountHolder);
    }

    public List<AccountHolder> getAllAccounts() {
        return repository.findAll();
    }

    public AccountHolder getAccountById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public void deleteAccount(Long id) {
        repository.deleteById(id);
    }
}