package com.example.Spring2.Controller;

import com.example.Spring2.entities.FilmScrape;
import com.example.Spring2.repository.ScrapeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping(path = "/scrape")
public class ScrapeController {

    @Autowired
    ScrapeSet scrapeSet;

    @GetMapping("year={anfang},{ende}/{anzahl}")
    Set<FilmScrape> getFilmByDatum(@PathVariable("anfang") String anfang,
                                   @PathVariable("ende") String ende,
                                   @PathVariable("anzahl") String anzahl) throws IOException {
        return scrapeSet.getFilmByDatum(anfang,ende,anzahl);
    }

    @GetMapping("genres={kategorie}/{anzahl}")
    Set<FilmScrape> getFilmByKategorie(@PathVariable("kategorie") String kategorie,
                                       @PathVariable("anzahl") String anzahl) throws IOException {
        return scrapeSet.getFilmByKategorie(kategorie,anzahl);
    }

    @GetMapping("year={anfang},{ende}/genres={kategorie}/{anzahl}")
    Set<FilmScrape> getFilmybyDatumAndKategorie(
            @PathVariable("anfang") String anfang,
            @PathVariable("ende") String ende,
            @PathVariable("kategorie") String kategorie,
            @PathVariable("anzahl") String anzahl) throws IOException {
        return scrapeSet.getFilmybyDatumAndKategorie(anfang,ende,kategorie,anzahl);
    }


}

