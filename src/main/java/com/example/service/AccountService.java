package com.example.service;

import javax.naming.AuthenticationException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateAccountException;
import com.example.repository.AccountRepository;

@Service
@Transactional
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account register(Account newAccount) throws DuplicateAccountException, Exception{
        if(!(newAccount.getUsername().isBlank()) && ((newAccount.getPassword().length() > 3))){
            if((accountRepository.findByUsername(newAccount.getUsername()).isEmpty())){
                accountRepository.save(newAccount);
                return accountRepository.getById(newAccount.getAccountId());
            } else{
                throw new DuplicateAccountException("Account Already Exists");
            }
        }
        throw new Exception();
    }

    public Account login(String username, String password) throws AuthenticationException{
        Account valid = accountRepository.findByUsernameAndPassword(username, password).
            orElseThrow(() -> new AuthenticationException());
        return accountRepository.getById(valid.getAccountId());
    }
}
