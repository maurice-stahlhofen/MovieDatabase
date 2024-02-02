package com.example.Spring2.ChatPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    private ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository){
        this.chatRepository=chatRepository;
    }

    public List<Chat> getChat(){
        return chatRepository.findAll();
    }

    public void addChat(Chat chat){
        chatRepository.save(chat);
    }

}
