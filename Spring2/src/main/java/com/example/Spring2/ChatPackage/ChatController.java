package com.example.Spring2.ChatPackage;
//erstmal alles auf einen Haufen in einem eigenen Package


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("controller/chat")
public class ChatController {
    private ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService){
        this.chatService=chatService;
    }

    @GetMapping
    public List<Chat> getChat(){
        return chatService.getChat();
    }

    @PostMapping("add")
    public void addChat(@RequestBody Chat chat){
        chatService.addChat(chat);
    }

}
