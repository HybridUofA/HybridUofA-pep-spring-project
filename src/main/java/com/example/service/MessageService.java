package com.example.service;

import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.entity.Message;
import com.example.exception.*;
import com.example.entity.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountRepository accountRepository;

    public Message createNewMessage(Message message) {
        if(message.getMessageText().length() >= 255 || message.getMessageText().isEmpty()) {
            throw new InvalidMessageException(message.getMessageText());
        }
        Optional<Account> posterAccount = accountRepository.findById(message.getPostedBy());
        if(posterAccount.isPresent()) {
            return messageRepository.save(message);
        }
        throw new UserNotFoundException(message.getPostedBy().toString());
  

    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int id) {
        Optional<Message> msg = messageRepository.findById(id);
        if(msg.isPresent()) {
            Message message = msg.get();
            return message;
        }
        return null;
    }

    public Integer deleteMessageById(int id) {
        Optional<Message> msg = messageRepository.findById(id);
        if(msg.isPresent()) {
            Message message = msg.get();
            messageRepository.delete(message);
            return 1;
        }
        return 0;
    }

    public Integer updateMessageById(int id, String message_text) {
        Optional<Message> msg = messageRepository.findById(id);
        if(msg.isPresent()) {
            Message message = msg.get();
            if(message_text == null || message_text.isBlank() || message_text.length() == 0) {
                throw new InvalidMessageException("Message cannot be empty or whitespace");
            }
            if(message_text.length() >= 255) {
                throw new InvalidMessageException("Message text exceeds the maximum allowed message length");
            }

            message.setMessageText(message_text);
            messageRepository.save(message);
            return 1;
        }
        throw new InvalidMessageException("Message not found");
    }

    public List<Message> getMessagesByAccountId(int id) {
        Optional<Account> acc = accountRepository.findById(id);
        if(acc.isPresent()) {
            Account msgAcc = acc.get();
            return messageRepository.getByPostedBy(msgAcc.getAccountId());
        }
        return new ArrayList<>();
    }
}
