package com.example.Spring2.repository;

import com.example.Spring2.entities.FilmScrape;

import java.io.IOException;
import java.util.Set;
//Aufbau ähnelt:
//https://medium.com/@sushain_Dilishan/building-web-scraping-api-with-spring-boot-jsoup-a0cc19dbd5dd

public interface ScrapeSet {

    Set<FilmScrape> getFilmByKategorie(String kategorie, String anzahl) throws IOException;

    Set<FilmScrape> getFilmByDatum(String anfang, String ende, String anzahl) throws IOException;

    Set<FilmScrape> getFilmybyDatumAndKategorie(String anfang, String ende, String kategorie, String anzahl) throws IOException;
}