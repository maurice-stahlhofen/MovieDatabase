package com.example.Spring2.Diskussion.Gruppe;

import com.example.Spring2.Diskussion.GruppenTeilnehmer.GruppenTeilnehmer;
import com.example.Spring2.Diskussion.GruppenTeilnehmer.GruppenTeilnehmerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("controller/diskussion/gruppe")
public class GruppenController {
    private GruppenService gruppenService;
    private GruppenTeilnehmerService gruppenTeilnehmerService;

    @Autowired
    public GruppenController(GruppenService gruppenService, GruppenTeilnehmerService gruppenTeilnehmerService) {
        this.gruppenService = gruppenService;
        this.gruppenTeilnehmerService = gruppenTeilnehmerService;
    }

    @GetMapping
    public List<Gruppe> getGruppe(){
        return gruppenService.getGruppen();
    }

    @PostMapping("add")
    public void addGruppe(@RequestBody Gruppe gruppe){
        gruppenService.add(gruppe);
    }

    @PutMapping("changeName/{id}")
    public void changeName(@PathVariable("id") Integer groupID, @RequestBody String name){
        gruppenService.changeName(groupID, name);
    }

    @GetMapping("byprivacy/{privacy}")
    public List<Gruppe> getByPrivacy(@PathVariable("privacy") String isPrivate){
        return gruppenService.getGroupsByPrivacy(isPrivate);
    }

    //für den modultest noch wichtig
    @GetMapping("bySpecificGroup/{name}/{privacy}/{creator}")
    public List<Gruppe> getByNamePrivacyCreator(@PathVariable("name") String name,
                                                @PathVariable("privacy") String privacy,
                                                @PathVariable("creator") String creator) {
        return gruppenService.getByNamePrivacyCreator(name,privacy,creator);
    }
    //ohne Name, da sonderzeichen wie üöä Fehler werfen, und emails hoffentlich auf
    //nicht sonderzeichen genormt sind!
    @GetMapping("bySpecificGroupOhneName/{privacy}/{creator}")
    public List<Gruppe> getByNamePrivacyCreator(@PathVariable("privacy") String privacy,
                                                @PathVariable("creator") String creator) {
        return gruppenService.getByPrivacyCreator(privacy,creator);
    }

    @GetMapping("filteredGroupsByName/{user}")
    public List<Gruppe> getFilteredGroups(@PathVariable("user") String email){
        List<GruppenTeilnehmer> groups=gruppenTeilnehmerService.findGroupsByParticipant(email);
        return gruppenService.getFilteredGroups(email, groups);
    }

    @GetMapping("filteredGroupsByID/{id}")
    public List<Gruppe> getFilteredGroupsById(@PathVariable("id") Integer id){
        return gruppenService.getFilteredGroupsById(id);
    }

    @ResponseBody
    @RequestMapping(value = "deleteByID/{id}",
            produces = "application/json",
            method={RequestMethod.DELETE,RequestMethod.GET})
    public void deleteGroup(@PathVariable("id") Integer id){
        gruppenService.delete(id);
    }




}
