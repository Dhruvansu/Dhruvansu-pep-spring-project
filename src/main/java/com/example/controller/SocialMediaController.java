package com.example.controller;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateAccountException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // Registering Account and Presesting it to Database
    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account account) throws DuplicateAccountException, Exception{
        Account presistedAccount = accountService.register(account);
        return ResponseEntity.ok().body(presistedAccount);
    }

    @ExceptionHandler(DuplicateAccountException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateAccount(DuplicateAccountException ex){
        return ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleClientError(Exception ex){
        return ex.getMessage();
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account) throws AuthenticationException{
        Account successfulLogin = accountService.login(account.getUsername(), account.getPassword());
        return ResponseEntity.ok().body(successfulLogin);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handledUnauthorized(AuthenticationException exception){
        return exception.getMessage();
    }

}
