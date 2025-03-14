package com.example.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
@Transactional
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message postMessage(Integer postedBy, String messageBody, Long postedTime) throws Exception{
        if(!(messageBody.isBlank()) && (messageBody.length() < 256) &&
             !(accountRepository.findById(postedBy).isEmpty())){
            Message newMessage = new Message(postedBy, messageBody, postedTime);
            messageRepository.save(newMessage);
            return messageRepository.getById(newMessage.getMessageId());
        }
        throw new Exception();
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageUsingMessageId(Integer messageId){
        return messageRepository.findById(messageId).orElse(null);
    }

    public Boolean deleteMessageUsingMessageId(Integer messageId){
        if(!(messageRepository.findById(messageId).isEmpty())){
            messageRepository.deleteById(messageId);
            return true;
        }
        return false;
    }

    public void updateMessageText(Integer messageId, String messageText) throws Exception{
        Message message = messageRepository.findById(messageId).orElseThrow();
        if(!(messageText.isEmpty()) && (messageText.length() < 256)){
            message.setMessageText(messageText);
            messageRepository.save(message);
        } else
            throw new Exception();
    }

    public List<Message> getAllMessageUsingAccountId(Integer accountId){
        return messageRepository.findByPostedBy(accountId).get();
    }
}
