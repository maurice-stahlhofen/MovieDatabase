package com.example.Spring2.Nachricht;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("controller/nachrichten")
public class NachrichtController {
    private NachrichtService nachrichtService;

    @Autowired
    public NachrichtController(NachrichtService nachrichtService){
        this.nachrichtService=nachrichtService;
    }

    @GetMapping
    public List<Nachricht> getNachricht(){
        return nachrichtService.getNachrichten();
    }

    @PostMapping("/add")
    public void addNachricht(@RequestBody Nachricht nachricht){
        nachrichtService.addNachricht(nachricht);
    }

    @GetMapping("/fromchat/{chatid}")
    public List<Nachricht> getNachrichtfromSpecifiedChat(@PathVariable("chatid") Integer chatID){
        return nachrichtService.getNachrichtfromSpecifiedChat(chatID);
    }

}
