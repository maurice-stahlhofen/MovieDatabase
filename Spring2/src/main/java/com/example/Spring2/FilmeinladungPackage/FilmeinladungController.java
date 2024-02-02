package com.example.Spring2.FilmeinladungPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("controller/filmeinladung")
public class FilmeinladungController {

    private FilmeinladungService filmeinladungService;

    @Autowired
    public FilmeinladungController(FilmeinladungService filmeinladungService){
        this.filmeinladungService = filmeinladungService;
    }

    @GetMapping
    public List<Filmeinladung> getFilmeinladung(){
        return filmeinladungService.getFilmeinladung();
    }

    @PostMapping("add")
    public void addFilmeinladung(@RequestBody Filmeinladung filmeinladung){
        filmeinladungService.addFilmeinladung(filmeinladung);
    }

    @PutMapping("aendern")
    public void aendereFilmeinladung(@RequestBody Filmeinladung filmeinladung){
        filmeinladungService.aendereFilmeinladung(filmeinladung);
    }

    @ResponseBody
    @RequestMapping(value = "delete/{id}", produces = "application/json", method = {RequestMethod.DELETE, RequestMethod.GET})
    public void loescheFIlmeinladung(@PathVariable("id") Integer id){
        filmeinladungService.delete(id);
    }

}