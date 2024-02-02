package com.example.Spring2.Controller;

import com.example.Spring2.entities.Film;
import com.example.Spring2.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("controller/film")
/*
Controller, Annotationen bei den Entitäten, Repositories
sind an diesen Aufbau angelehnt:
https://www.baeldung.com/rest-http-put-vs-post
 */
public class FilmController {

    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilm(){
        return filmService.getFilm();
    }

    @PostMapping("add")
    public void addFilm(@RequestBody Film film){
        filmService.addFilm(film);
    }

    @PutMapping("aendern")
    public void aendereFilm(@RequestBody Film film){
        filmService.aendereFilm(film);
    }

    @GetMapping("name")
    public List<Film> getfilmbyName(@PathVariable String name){
        return filmService.getFilmbyName(name);
    }

    @GetMapping("id/{filmid}")
    public List<Film> getFilmbyId(@PathVariable("filmid") int id){
        return filmService.getFilmbyId(id);
    }

    @ResponseBody
    @RequestMapping(value = "delete/{id}",
            produces = "application/json",
            method={RequestMethod.DELETE,RequestMethod.GET})
    public void deleteFilm(@PathVariable("id") Integer id){
        filmService.deleteByID(id);
    }

}