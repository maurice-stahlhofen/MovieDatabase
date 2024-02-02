package com.example.Spring2.Controller;

import com.example.Spring2.entities.Nutzer;
import com.example.Spring2.service.NutzerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
Controller, Annotationen bei den Entitäten, Repositories
sind an diesen Aufbau angelehnt:
https://www.baeldung.com/rest-http-put-vs-post
 */

@RestController
@RequestMapping("controller/nutzer")
public class NutzerController {

    private NutzerService nutzerService;

    public NutzerController(NutzerService nutzerService) {
        this.nutzerService = nutzerService;
    }
    //hier bitte keine leeren Konstruktoren, außer man möchte eine Whitelabel Website haben


    @GetMapping
    public List<Nutzer> getNutzer() {
        return nutzerService.getNutzer();
    }

    @PostMapping("add")
    public void addnutzer(@RequestBody Nutzer nutzer) {
        nutzerService.addNutzer(nutzer);
    }

    @PutMapping("aendern")
    public void aenderenutzer(@RequestBody Nutzer nutzer) {
        nutzerService.aendereNutzer(nutzer);
    }

    @DeleteMapping("delete")
    public void deleteNutzer(@RequestBody Nutzer nutzer) {
        nutzerService.deleteNutzer(nutzer);
    }

    @GetMapping("email")
    public List<Nutzer> getNutzerbyEmail(@PathVariable("email") String email) {
        return nutzerService.getNutzerbyEmail(email);
    }

    @GetMapping(value = "email/{email}")
    public List<Nutzer> getNutzerbyId(@PathVariable("email") String email) {
        return nutzerService.getNutzerbyEmail(email);
    }
}
