package com.example.Spring2.FehlermeldungPackage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("controller/fehlermeldung")
public class FehlermeldungController {

    private FehlermeldungService fehlermeldungService;

    @Autowired
    public FehlermeldungController(FehlermeldungService fehlermeldungService){
        this.fehlermeldungService = fehlermeldungService;
    }

    @GetMapping
    public List<Fehlermeldung> getFehlermeldungen(){
        return fehlermeldungService.getFehlermeldungen();
    }

    @PostMapping("add")
    public void addFehlermeldung(@RequestBody Fehlermeldung fehlermeldung){
        fehlermeldungService.addFehlermeldung(fehlermeldung);
    }

    @PostMapping("delet")
    public void deleteFehlermeldung(@RequestBody Fehlermeldung fehlermeldung){
        fehlermeldungService.deleteFehlermeldung(fehlermeldung);
    }

}
