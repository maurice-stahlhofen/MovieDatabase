package com.example.Spring2.service;

import com.example.Spring2.entities.Nutzer;
import com.example.Spring2.repository.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NutzerService {

        private NutzerRepository nutzerRepository;

        @Autowired
        public NutzerService(NutzerRepository nutzerRepository){
                this.nutzerRepository=nutzerRepository;
        }

        public List<Nutzer> getNutzer(){
                return nutzerRepository.findAll();
        }

        public void addNutzer(Nutzer nutzer){
                nutzerRepository.save(nutzer);
        }


        public void deleteNutzer(Nutzer nutzer){
                if(nutzerRepository.findById(nutzer.getEmail()).toString().isEmpty()){
                        System.out.println("Nutzer existiert nicht.");
                }
                else{
                        nutzerRepository.deleteById(nutzer.getEmail());
                        System.out.println("Nutzer wurde gelöscht!");
                }
        }

        public void aendereNutzer(Nutzer nutzer){
                if(nutzerRepository.findById(nutzer.getEmail()).toString().isEmpty()){
                        System.out.println("Nutzer wurde noch nicht angelegt.");
                }else{
                        nutzerRepository.save(nutzer);
                }
        }

        public List<Nutzer> getNutzerbyEmail(String email) {
                List<Nutzer>alle=nutzerRepository.findAll();
                List<Nutzer> gesucht=new ArrayList<>();
                for(Nutzer nutzer:alle){
                        if(nutzer.getEmail().equals(email)){
                                gesucht.add(nutzer);
                        }
                }
                return gesucht;
        }


}