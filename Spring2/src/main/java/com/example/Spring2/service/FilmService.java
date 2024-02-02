package com.example.Spring2.service;

import com.example.Spring2.entities.Film;
import com.example.Spring2.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class FilmService {

    private FilmRepository filmRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository){
        this.filmRepository=filmRepository;
    }

    public List<Film> getFilm(){
        return filmRepository.findAll();
    }

    public void addFilm(Film film){
        filmRepository.save(film);
    }

    public void deleteFilm(Film film){
        if(filmRepository.findById(film.getFilmId()).isEmpty()){
            System.out.println("Film existiert nicht.");
        }
        else{
            filmRepository.deleteById(film.getFilmId());
            System.out.println("Film wurde gelöscht!");
        }
    }

    public void aendereFilm(Film film){
        if(filmRepository.findById(film.getFilmId()).isEmpty()){
            System.out.println("Film wurde noch nicht angelegt.");
        }else{
            filmRepository.save(film);
        }
    }

//        public List<Film> getFilmbyID(int id){
//            List<Film> alle=filmRepository.findAll();
//            List<Film> gesucht=new ArrayList<Film>();
//            for(Film filme: alle){
//                if(filme.getFilmId()==(id)){
//                    gesucht.add(filme);
//            }
//        }
//            return gesucht;
//        }

    public List<Film> getFilmbyName(String name){
        List<Film> alle=filmRepository.findAll();
        List<Film>gesucht=new ArrayList<>();
        for(Film filme:alle){
            if(filme.getName().equals(name)){
                gesucht.add(filme);
            }
        }
        return gesucht;
    }


    public List<Film> getFilmbyKategorie(String kategorie){
        List<Film> alle=filmRepository.findAll();
        List<Film> gesucht=new ArrayList<Film>();
        for(Film filme: alle){
            if(filme.getKategorie().equals(kategorie)){
                gesucht.add(filme);
            }
        }
        return gesucht;
    }

    public List<Film> getFilmbyDatum(String datum){
        List<Film> alle=filmRepository.findAll();
        List<Film> gesucht=new ArrayList<Film>();
        for(Film filme: alle){
            if(filme.getErscheinungsdatum().equals(datum)){
                gesucht.add(filme);
            }
        }
        return gesucht;
    }

    public List<Film> getFilmbyKategorieUndDatum(String kategorie, String datum){
        List<Film> alle=filmRepository.findAll();
        List<Film> gesucht=new ArrayList<Film>();
        for(Film filme: alle){
            if(filme.getErscheinungsdatum().equals(datum) && filme.getKategorie().equals(kategorie)){
                gesucht.add(filme);
            }
        }
        return gesucht;
    }


    public List<Film> getFilmbyId(int id) {
        Optional<Film> alle=filmRepository.findById(id);
        List<Film> gesucht=alle.stream().collect(Collectors.toList());
        return gesucht;
    }

    public void deleteByID(Integer id) {
        filmRepository.deleteById(id);
    }
}