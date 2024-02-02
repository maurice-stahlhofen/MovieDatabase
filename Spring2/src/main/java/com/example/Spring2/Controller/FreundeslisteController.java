package com.example.Spring2.Controller;


import com.example.Spring2.entities.Freundesliste;
import com.example.Spring2.service.FreundeslisteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="controller/freundesliste")
public class FreundeslisteController {

    private FreundeslisteService freundeslisteService;

    public FreundeslisteController(FreundeslisteService freundeslisteService) {
        this.freundeslisteService = freundeslisteService;
    }

    @GetMapping
    public List<Freundesliste> getFreundesliste(){
        return freundeslisteService.getFreundesliste();
    }

    @PostMapping("add")
    public void addFreundesliste(@RequestBody Freundesliste freundesliste){
        freundeslisteService.addFreundesliste(freundesliste);
    }

    @GetMapping("byEmail/{email}")
    public List<Freundesliste> getFreundeslistebyEmail(@PathVariable("email") String email){
        return freundeslisteService.getFreundeslistebyEmail(email);
    }



}
