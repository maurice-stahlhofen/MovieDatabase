package com.example.Spring2.FilmBewertungenPackage;

import com.example.Spring2.FilmBewertungenPackage.Bewertung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BewertungRepository extends JpaRepository<Bewertung, Integer> {


    @Modifying
    @Query("delete from Bewertung bw where bw.nutzerEmail = ?1")
    void deleteByEmail(String email);



}
