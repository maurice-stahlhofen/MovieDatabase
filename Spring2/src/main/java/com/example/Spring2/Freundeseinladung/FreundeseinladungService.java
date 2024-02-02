package com.example.Spring2.Freundeseinladung;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FreundeseinladungService {
    
    private FreundeseinladungRepository freundeseinladungRepository;

    public FreundeseinladungService(FreundeseinladungRepository freundeseinladungRepository) {
        this.freundeseinladungRepository = freundeseinladungRepository;
    }


    public List<Freundeseinladung> getFreundeseinladung() {
        return freundeseinladungRepository.findAll();
    }

    public List<Freundeseinladung> getFreundeseinladungByID(Integer einladungsID) {
        List<Freundeseinladung> all=freundeseinladungRepository.findAll();
        List<Freundeseinladung> wanted=new ArrayList<>();
        for(Freundeseinladung search:all){
            if(einladungsID.equals(search.getId())){
                wanted.add(search);
            }
        }
        return wanted;
    }

    public void addFreundeseinladung(Freundeseinladung freundeseinladung) {
        freundeseinladungRepository.save(freundeseinladung);
    }

    public void delete(Freundeseinladung freundeseinladung) {
        freundeseinladungRepository.delete(freundeseinladung);
    }

    public void deleteByID(Integer id) {
        freundeseinladungRepository.deleteById(id);
    }
}
