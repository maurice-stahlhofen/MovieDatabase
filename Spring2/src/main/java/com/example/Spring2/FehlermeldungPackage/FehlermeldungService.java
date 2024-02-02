package com.example.Spring2.FehlermeldungPackage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FehlermeldungService {

    private FehlermeldungRepository fehlermeldungRepository;

    @Autowired
    public FehlermeldungService(FehlermeldungRepository fehlermeldungRepository){
        this.fehlermeldungRepository=fehlermeldungRepository;
    }

    public List<Fehlermeldung> getFehlermeldungen() {
        return fehlermeldungRepository.findAll();
    }

    public void addFehlermeldung(Fehlermeldung fehlermeldung){
        fehlermeldungRepository.save(fehlermeldung);
    }

    public void deleteFehlermeldung(Fehlermeldung fehlermeldung) {
        if(fehlermeldungRepository.findById(fehlermeldung.getFehlermeldungsId()).isEmpty()){
            System.out.println("Fehlermeldung existiert nicht");
        }
        else{
            fehlermeldungRepository.deleteById(fehlermeldung.getFehlermeldungsId());
            System.out.println("Fehlermeldung wurde gelöscht");
        }
    }

}
