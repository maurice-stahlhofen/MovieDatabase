package com.example.Spring2.Diskussion.GruppenNachricht;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//quasi wie Nachricht
@RestController
@RequestMapping("controller/diskussion/nachrichten")
public class GruppenNachrichtController {
    private GruppenNachrichtService gruppenNachrichtService;

    @Autowired
    public GruppenNachrichtController(GruppenNachrichtService gruppenNachrichtService) {
        this.gruppenNachrichtService = gruppenNachrichtService;
    }

    @GetMapping
    public List<GruppenNachricht> getGruppenNachrichten(){
        return gruppenNachrichtService.getGruppenNachrichten();
    }

    @PostMapping("add")
    public void addGruppenNachricht(@RequestBody GruppenNachricht gruppenNachricht){
        gruppenNachrichtService.addGruppenNachricht(gruppenNachricht);
    }

    @GetMapping("fromgroup/{groupid}")
    public List<GruppenNachricht> getGruppenNachrichtfromSpecifiedChat(@PathVariable("groupid") String groupID){
        Integer id=Integer.parseInt(groupID);
        return gruppenNachrichtService.getGruppenNachrichtfromSpecifiedGroup(id);
    }
}
