package com.example.Spring2.Diskussion.Gruppe;

import com.example.Spring2.Diskussion.GruppenTeilnehmer.GruppenTeilnehmer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GruppenService {
    private GruppenRepository gruppenRepository;

    @Autowired
    public GruppenService(GruppenRepository gruppenRepository) {
        this.gruppenRepository = gruppenRepository;
    }

    public List<Gruppe> getGruppen() {
        return gruppenRepository.findAll();
    }

    public void add(Gruppe gruppe) {
        gruppenRepository.save(gruppe);
    }

    public void changeName(Integer groupID, String name) {
        gruppenRepository.getById(groupID).setName(name);
    }

    public List<Gruppe> getGroupsByPrivacy(String isPrivate) {
        Boolean privacy=Boolean.parseBoolean(isPrivate);

        List<Gruppe> allegruppen=gruppenRepository.findAll();
        List<Gruppe> wanted=new ArrayList<>();
        for(Gruppe found:allegruppen){
            if(found.getIsprivate().equals(privacy)){
                wanted.add(found);
            }
        }
        return wanted;
    }

    public List<Gruppe> getByNamePrivacyCreator(String name, String isPrivate, String creator) {
        Boolean privacy=Boolean.parseBoolean(isPrivate);

        List<Gruppe> allegruppen=gruppenRepository.findAll();
        List<Gruppe> wanted=new ArrayList<>();

        for(Gruppe found:allegruppen){
            String foundname=found.getName();
            Boolean foundprivacy=found.getIsprivate();
            String foundcreator=found.getCreatorMail();

            if(foundname.equals(name) && foundprivacy.equals(privacy) && foundcreator.equals(creator)){
                wanted.add(found);
            }
        }
        return wanted;
    }

    //wegen den Sonderzeichen die beim Erstellen einer Diskussiongruppe
    //zu fehlern in dem http-Seitenaufruf führen
    public List<Gruppe> getByPrivacyCreator(String isPrivate, String creator) {
        Boolean privacy=Boolean.parseBoolean(isPrivate);

        List<Gruppe> allegruppen=gruppenRepository.findAll();
        List<Gruppe> wanted=new ArrayList<>();

        for(Gruppe found:allegruppen){
            Boolean foundprivacy=found.getIsprivate();
            String foundcreator=found.getCreatorMail();

            if(foundprivacy.equals(privacy) && foundcreator.equals(creator)){
                wanted.add(found);
            }
        }
        return wanted;
    }

    public List<Gruppe> getFilteredGroups(String email, List<GruppenTeilnehmer> groups) {
        List<Gruppe> allgroups=gruppenRepository.findAll();
        List<Gruppe> wanted=new ArrayList<>();

        //filtert nach Gruppen in denen man nicht Mitglied ist
        for(Gruppe found: allgroups){
            boolean isin=false;
            for(GruppenTeilnehmer participant:groups){
                if(participant.getGroupID().equals(found.getID())){
                    isin=true;
                    break;
                }
            }
            if(!isin){
                wanted.add(found);
            }
        }
        //entfernt alle privaten Gruppen in der Liste
        wanted.removeIf(Gruppe::getIsprivate);

        return wanted;
    }


    public List<Gruppe> getFilteredGroupsById(Integer id) {
        List<Gruppe> allgroups=gruppenRepository.findAll();
        List<Gruppe> wanted=new ArrayList<>();

        for(Gruppe found: allgroups){
            if(found.getID().equals(id)){
                wanted.add(found);
                break;
            }
        }
        return wanted;
    }

    public void delete(Integer id) {
        gruppenRepository.deleteById(id);
    }
}
