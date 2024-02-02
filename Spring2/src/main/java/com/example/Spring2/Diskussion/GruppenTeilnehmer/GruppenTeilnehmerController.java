package com.example.Spring2.Diskussion.GruppenTeilnehmer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("controller/diskussion/teilnehmer")
public class GruppenTeilnehmerController {
    private GruppenTeilnehmerService gruppenTeilnehmerService;

    @Autowired
    public GruppenTeilnehmerController(GruppenTeilnehmerService gruppenTeilnehmerService) {
        this.gruppenTeilnehmerService = gruppenTeilnehmerService;
    }

    @GetMapping
    public List<GruppenTeilnehmer> getGruppenTeilnehmer(){
        return gruppenTeilnehmerService.getGruppenTeilnehmer();
    }

    @PostMapping("add")
    public void addGruppenTeilnehmer(@RequestBody GruppenTeilnehmer gruppenTeilnehmer){
        gruppenTeilnehmerService.addGruppenTeilnehmer(gruppenTeilnehmer);
    }

    @GetMapping("findByGroupID/{groupID}")
    public List<GruppenTeilnehmer> findTeilnehmerByGroups(@PathVariable("groupID") String groupID){
        //parseInt, da der Server/die Methode keine Zahl aus dem String extrahieren kann,
        //sondern dies immer in einem String resultiert
        Integer id=Integer.parseInt(groupID);
        return gruppenTeilnehmerService.findTeilnehmerByGroups(id);
    }

    @GetMapping("findByParticipant/{participantEmail}")
    public List<GruppenTeilnehmer> findParticipantByGroups(@PathVariable("participantEmail") String participant){
        return gruppenTeilnehmerService.findGroupsByParticipant(participant);
    }

    @ResponseBody
    @RequestMapping(value = "delete/{groupID}/{email}",
            produces = "application/json",
            method={RequestMethod.DELETE,RequestMethod.GET})
    public void delete(@PathVariable("groupID")Integer groupID, @PathVariable("email") String email){
        gruppenTeilnehmerService.delete(groupID, email);
    }


}
