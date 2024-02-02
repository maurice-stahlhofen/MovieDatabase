package com.example.Spring2.Freundeseinladung;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("controller/freundeseinladungen")
public class FreundeseinladungController {
    private FreundeseinladungService freundeseinladungService;

    @Autowired
    public FreundeseinladungController(FreundeseinladungService freundeseinladungService) {
        this.freundeseinladungService = freundeseinladungService;
    }

    @GetMapping
    public List<Freundeseinladung> getFreundeseinladung(){
        return freundeseinladungService.getFreundeseinladung();
    }

    @PostMapping("add")
    public void addFreundeseinladung(@RequestBody Freundeseinladung freundeseinladung){
        freundeseinladungService.addFreundeseinladung(freundeseinladung);
    }

    @ResponseBody
    @RequestMapping(value = "delete/{id}",
            produces = "application/json",
            method={RequestMethod.DELETE,RequestMethod.GET})
    public void declineFreundeseinladung(@PathVariable("id") Integer id){
        freundeseinladungService.deleteByID(id);
    }
    @ResponseBody
    @RequestMapping(value = "delete",
            produces = "application/json",
            method={RequestMethod.DELETE,RequestMethod.GET})
    public void declineFreundeseinladung(@RequestBody Freundeseinladung freundeseinladung){
        freundeseinladungService.delete(freundeseinladung);
    }

    @GetMapping("byID/{einladungsID}")
    public List<Freundeseinladung> getFreundeseinladungByID(@PathVariable("einladungsID")Integer einladungsID){
        return freundeseinladungService.getFreundeseinladungByID(einladungsID);
    }
}
