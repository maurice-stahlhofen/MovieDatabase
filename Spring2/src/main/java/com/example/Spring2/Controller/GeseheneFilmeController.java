package com.example.Spring2.Controller;


import com.example.Spring2.entities.GeseheneFilme;

import com.example.Spring2.service.GeseheneFilmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("controller/geseheneFilme")
public class GeseheneFilmeController {

    private GeseheneFilmeService geseheneFilmeService;

    @Autowired
    public GeseheneFilmeController(GeseheneFilmeService geseheneFilmeService) {
        this.geseheneFilmeService = geseheneFilmeService;
    }

    @GetMapping
    public List<GeseheneFilme> getGeseheneFilme(){
        return geseheneFilmeService.getGeseheneFilme();
    }

    @PostMapping("add")
    public void addGeseheneFilme(@RequestBody GeseheneFilme geseheneFilme){
        geseheneFilmeService.addGeseheneFilme(geseheneFilme);
    }

    @GetMapping("/email/{email}")
    public List<GeseheneFilme> getGeseheneFilmeByEmail(@PathVariable String email){
        return geseheneFilmeService.getGeseheneFilmeByEmail(email);
    }

  @GetMapping("/{filmid}")
    public List<GeseheneFilme> getGeseheneFilmeByFilmID(@PathVariable int filmid){
        return geseheneFilmeService.getGeseheneFilmeByFilmID(filmid);
    }
    @GetMapping("/delete/{filmid}")
    public String deleteGeseheneFilme(@PathVariable int filmid){
        geseheneFilmeService.deleteGeseheneFilme(filmid);
        return "gelöscht";
    }

    @GetMapping("/checkDuplicate/{filmid}/{email}")
    public String checkDuplicate(@PathVariable int filmid, @PathVariable String email){
       return geseheneFilmeService.checkDuplicate(filmid, email);

    }
    @ResponseBody
    @RequestMapping(value = "deleteTEST/{id}",
            produces = "application/json",
            method={RequestMethod.DELETE,RequestMethod.GET})
    public void declineFreundeseinladung(@PathVariable("id") Integer id){
        geseheneFilmeService.deleteByID(id);
    }




}
