package com.example.Spring2.FilmBewertungenPackage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="controller/bewertung")
public class BewertungController {

    private BewertungService bewertungService;

    @Autowired
    public BewertungController(BewertungService bewertungService) {
        this.bewertungService = bewertungService;
    }

    @GetMapping
    public List<Bewertung> getBewertung(){
        return bewertungService.getBewertung();
    }

    @PostMapping("add")
    public void addBewertung(@RequestBody Bewertung bewertung){
        bewertungService.addBewertung(bewertung);
    }

    @PostMapping("add/{filmid}")
    public void addBewertungMitFilmid(@RequestBody Bewertung bewertung, @PathVariable int filmid){
        bewertungService.addBewertungMitFilmid(bewertung, filmid);
    }

    @PutMapping("aendern")
    public void aendereBewertung(@RequestBody Bewertung bewertung){
        bewertungService.aendereBewertung(bewertung);
    }

  @GetMapping("/{filmid}")
    public List<Bewertung> getBewertungByFilmID(@PathVariable int filmid){
        return bewertungService.getBewertungByFilmID(filmid);
    }

   @GetMapping("/delete/{filmid}")
    public String deleteBewertung(@PathVariable int filmid){
        bewertungService.deleteBewertung(filmid);
        return "gelöscht";
    }


    /*
    @ResponseBody
    @RequestMapping(value = "/delete/mail/{email}",
            produces = "application/json",
            method={RequestMethod.DELETE,RequestMethod.GET})
    public void deleteByEmail(@PathVariable("email") String email){
        bewertungService.deleteByEmail(email);
    }
*/

}
