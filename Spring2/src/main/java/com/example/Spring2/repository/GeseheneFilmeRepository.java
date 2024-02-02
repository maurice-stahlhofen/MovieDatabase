package com.example.Spring2.repository;

import com.example.Spring2.entities.GeseheneFilme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeseheneFilmeRepository extends JpaRepository<GeseheneFilme, Integer> {


  //  List<GeseheneFilme> findByFilmID(int id);
}
