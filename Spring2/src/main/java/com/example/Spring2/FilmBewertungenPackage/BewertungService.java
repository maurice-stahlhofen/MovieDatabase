package com.example.Spring2.FilmBewertungenPackage;

import com.example.Spring2.entities.Film;
import com.example.Spring2.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BewertungService {

    private BewertungRepository bewertungRepository;

    private FilmRepository filmRepository;

    @Autowired
    public BewertungService(BewertungRepository bewertungRepository, FilmRepository filmRepository) {
        this.bewertungRepository = bewertungRepository;
        this.filmRepository = filmRepository;
    }

    public List<Bewertung> getBewertung() {
        return bewertungRepository.findAll();
    }

    public void addBewertung(Bewertung bewertung) {
        bewertungRepository.save(bewertung);
    }
    public void addBewertungMitFilmid(Bewertung bewertung, int filmid) {

        List<Film> alle=filmRepository.findAll();
        List<Film> gesucht=new ArrayList<>();
        for(Film film:alle){
            if(film.getFilmId()==filmid){
                gesucht.add(film);
            }
        }
            //die gesucht-Liste hat hier immer nur inen Eintrag, da filmid nur 1 Mal vorkommen kann
        
            bewertung.setFilmId(gesucht.get(0));
            bewertungRepository.save(bewertung);

    }

    public void aendereBewertung(Bewertung bewertung) {
        if (bewertungRepository.findById(bewertung.getBewertungsId()).isEmpty()) {
            System.out.println("Bewertung wurde noch nicht angelegt.");
        } else {
            bewertungRepository.save(bewertung);
        }
    }


    public List<Bewertung> getBewertungByFilmID(int FilmID) {
        List<Bewertung> alle=bewertungRepository.findAll();
        List<Bewertung> gesucht=new ArrayList<>();
        for(Bewertung bw:alle){
            if(bw.getFilmId().getFilmId()==FilmID){
                gesucht.add(bw);
            }
        }
        return gesucht;

    }
    public void deleteBewertung(int filmid){
        List<Bewertung> alleMitDerFilmID =  getBewertungByFilmID(filmid);
        for(Bewertung bw: alleMitDerFilmID){
            bewertungRepository.delete(bw);
        }
    }

    public void deleteByEmail(String email) {
        bewertungRepository.deleteByEmail(email);
    }


}


