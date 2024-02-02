package com.example.Spring2.Diskussion.GruppenNachricht;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//wie bei Nachrichten
@Service
public class GruppenNachrichtService {
    private GruppenNachrichtRepository gruppenNachrichtRepository;

    @Autowired
    public GruppenNachrichtService(GruppenNachrichtRepository gruppenNachrichtRepository) {
        this.gruppenNachrichtRepository = gruppenNachrichtRepository;
    }

    public List<GruppenNachricht> getGruppenNachrichten() {
        return gruppenNachrichtRepository.findAll();
    }

    public void addGruppenNachricht(GruppenNachricht gruppenNachricht) {
        gruppenNachrichtRepository.save(gruppenNachricht);
    }

    public List<GruppenNachricht> getGruppenNachrichtfromSpecifiedGroup(Integer groupID) {
        List<GruppenNachricht> all=gruppenNachrichtRepository.findAll();
        List<GruppenNachricht> wanted=new ArrayList<>();

        for(GruppenNachricht found:all){
            if(found.getGroupID().equals(groupID)){
                wanted.add(found);
            }
        }
        return wanted;
    }
}
