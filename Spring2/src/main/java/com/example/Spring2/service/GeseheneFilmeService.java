package com.example.Spring2.service;


import com.example.Spring2.entities.GeseheneFilme;
import com.example.Spring2.repository.GeseheneFilmeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeseheneFilmeService {

    private GeseheneFilmeRepository geseheneFilmeRepository;

    @Autowired
    public GeseheneFilmeService(GeseheneFilmeRepository geseheneFilmeRepository){
        this.geseheneFilmeRepository=geseheneFilmeRepository;
    }

    public List<GeseheneFilme> getGeseheneFilme(){
        return geseheneFilmeRepository.findAll();
    }

    public void addGeseheneFilme(GeseheneFilme geseheneFilme){
        geseheneFilmeRepository.save(geseheneFilme);
    }


    public List<GeseheneFilme> getGeseheneFilmeByEmail(String email){
    List<GeseheneFilme> alle=geseheneFilmeRepository.findAll();
    List<GeseheneFilme>gesucht=new ArrayList<>();
        for(GeseheneFilme gf:alle){
        if(gf.getEmailNutzer().equals(email)){
            gesucht.add(gf);
        }
    }
        return gesucht;
}

   public List<GeseheneFilme> getGeseheneFilmeByFilmID(int filmid) {
       List<GeseheneFilme> alle=geseheneFilmeRepository.findAll();
       List<GeseheneFilme> gesucht=new ArrayList<>();
       for(GeseheneFilme gfilme:alle){
           if(gfilme.getFilmId().getFilmId()==filmid){
               gesucht.add(gfilme);
           }
       }
       return gesucht;
    }

    public void deleteGeseheneFilme(int filmid){
        List<GeseheneFilme> alleMitDerFilmID =  getGeseheneFilmeByFilmID(filmid);
        for(GeseheneFilme gf: alleMitDerFilmID){
            geseheneFilmeRepository.delete(gf);
        }
    }


    //Check, ob Film bereits als gesehen markiert wurde
    public String checkDuplicate(int filmid, String email){
        // Alle Filme holen
        List<GeseheneFilme> alle = geseheneFilmeRepository.findAll();
        // Einzeln durchlaufen
        for(GeseheneFilme gesfilm : alle){
            // Wenn Email und FilmID in einem Tupel existieren, gib true aus. Denn dann würde es doppelt angelegt werden
            if(gesfilm.getEmailNutzer().equals(email) && gesfilm.getFilmId().getFilmId()==filmid){
                return "true";
            }
        }
        return "false";
    }

    public void deleteByID(Integer id) {
        geseheneFilmeRepository.deleteById(id);
    }
}
