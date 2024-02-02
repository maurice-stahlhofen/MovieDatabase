package com.example.Spring2.service;


import com.example.Spring2.entities.Film;
import com.example.Spring2.entities.Freundesliste;
import com.example.Spring2.repository.FilmRepository;
import com.example.Spring2.repository.FreundeslisteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FreundeslisteService {


    private FreundeslisteRepository freundeslisteRepository;

    @Autowired
    public FreundeslisteService(FreundeslisteRepository freundeslisteRepository){
        this.freundeslisteRepository=freundeslisteRepository;
    }

    public List<Freundesliste> getFreundesliste(){
        return freundeslisteRepository.findAll();
    }

    public void addFreundesliste(Freundesliste freundesliste){
        freundeslisteRepository.save(freundesliste);
    }

    public List<Freundesliste> getFreundeslistebyEmail(String email){
        List<Freundesliste> alle=freundeslisteRepository.findAll();
        List<Freundesliste>gesucht=new ArrayList<>();
        for(Freundesliste tmp:alle){
            if(tmp.getEmailNutzer().equals(email)){
                gesucht.add(tmp);
            }
        }
        return gesucht;
    }


}
