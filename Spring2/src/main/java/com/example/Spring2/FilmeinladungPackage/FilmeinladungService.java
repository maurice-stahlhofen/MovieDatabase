package com.example.Spring2.FilmeinladungPackage;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmeinladungService {

    private FilmeinladungRepository filmeinladungRepository;

    public FilmeinladungService(FilmeinladungRepository filmeinladungRepository){
        this.filmeinladungRepository = filmeinladungRepository;
    }

    public List<Filmeinladung> getFilmeinladung(){
        return filmeinladungRepository.findAll();
    }

    public void addFilmeinladung(Filmeinladung filmeinladung){
        filmeinladungRepository.save(filmeinladung);
    }

    public void aendereFilmeinladung(Filmeinladung filmeinladung){
        if(filmeinladungRepository.findById(filmeinladung.getId()).isEmpty()){
            System.out.println("Einladung existiert nicht");
        }else {
            filmeinladungRepository.save(filmeinladung);
        }
    }

    public void delete(Integer id){
        filmeinladungRepository.deleteById(id);
    }
}
