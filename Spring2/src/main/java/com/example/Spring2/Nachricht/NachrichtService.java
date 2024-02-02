package com.example.Spring2.Nachricht;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NachrichtService {

    private NachrichtRepository nachrichtRepository;

    @Autowired
    public NachrichtService(NachrichtRepository nachrichtRepository){
        this.nachrichtRepository=nachrichtRepository;
    }

    public List<Nachricht> getNachrichten(){
        return nachrichtRepository.findAll();
    }

    public void addNachricht(Nachricht nachricht){
        nachrichtRepository.save(nachricht);
    }

    public List<Nachricht> getNachrichtfromSpecifiedChat(Integer id){
        List<Nachricht> all=this.getNachrichten();
        List<Nachricht> wanted=new ArrayList<>();

        for(Nachricht nachricht:all){
            if(nachricht.getChatID().equals(id)){
                wanted.add(nachricht);
            }
        }
        return wanted;


    }



}
