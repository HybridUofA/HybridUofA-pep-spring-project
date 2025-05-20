package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.exception.*;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount(Account account) {
        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());
        if(existingAccount.isPresent()) {
            throw new DuplicateUserException(account.getUsername());
        }
        return accountRepository.save(account);
    }

    public Account loginToAccount(String username, String password) {
        Optional<Account> existingAccount = accountRepository.findByUsername(username);
        if(existingAccount.isEmpty()) {
            throw new UnauthorizedLogin("Invalid username or password");
        }
        Account loginAccount = existingAccount.get();
        if(password.equals(loginAccount.getPassword())) {
            return loginAccount;
        }
        throw new UnauthorizedLogin("Invalid username or password");
    }
}

