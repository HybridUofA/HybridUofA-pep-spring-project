package com.example.controller;

import com.example.entity.*;
import com.example.exception.*;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }
    @PostMapping("/register")
    public ResponseEntity<Object> registerAccount(@RequestBody Account account) {
        try {
            Account newAccount = accountService.registerAccount(account);
            return ResponseEntity.ok(newAccount);
        } catch(DuplicateUserException e) {
            return ResponseEntity.status(409).body("Duplicate User Found: " + e.getMessage());
        } catch(Exception e) {
            return ResponseEntity.badRequest().body("Unknown error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginAccount(@RequestBody Account account) {
        try {
            Account loginAccount = accountService.loginToAccount(account.getUsername(), account.getPassword());
            return ResponseEntity.ok(loginAccount);
        } catch (UnauthorizedLogin e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Object> createNewMessage(@RequestBody Message message) {
        try {
            Message createdMessage = messageService.createNewMessage(message);
            return ResponseEntity.ok(createdMessage);
        } catch(InvalidMessageException e) {
            return ResponseEntity.badRequest().body("Invalid message text: " + e.getMessage());
        } catch(UserNotFoundException e) {
            return ResponseEntity.badRequest().body("User does not exist: " + e.getMessage());
        }

    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer message_id) {
        Message message = messageService.getMessageById(message_id);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Integer message_id) {
        Integer rowsDeleted = messageService.deleteMessageById(message_id);
        if(rowsDeleted == 1) {
            return ResponseEntity.ok(rowsDeleted.toString());        
        }
        return ResponseEntity.ok("");

    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Object> updateMessage(@PathVariable Integer message_id, @RequestBody Map<String, String> body) {
        String message_text = body.get("messageText");
        try {
            Integer updated_rows = messageService.updateMessageById(message_id, message_text);
            return ResponseEntity.ok().body(updated_rows);        
        } catch(InvalidMessageException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessagesFromUserId(@PathVariable Integer account_id) {
        List<Message> userMessages = messageService.getMessagesByAccountId(account_id);
        return ResponseEntity.ok().body(userMessages);
    }
}
