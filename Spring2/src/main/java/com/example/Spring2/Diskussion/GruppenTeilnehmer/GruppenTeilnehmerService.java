package com.example.Spring2.Diskussion.GruppenTeilnehmer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GruppenTeilnehmerService {
    private GruppenTeilnehmerRepository gruppenTeilnehmerRepository;

    @Autowired
    public GruppenTeilnehmerService(GruppenTeilnehmerRepository gruppenTeilnehmerRepository) {
        this.gruppenTeilnehmerRepository = gruppenTeilnehmerRepository;
    }

    public List<GruppenTeilnehmer> getGruppenTeilnehmer(){
        return gruppenTeilnehmerRepository.findAll();
    }

    public void addGruppenTeilnehmer(GruppenTeilnehmer gruppenTeilnehmer){
        gruppenTeilnehmerRepository.save(gruppenTeilnehmer);
    }

    public List<GruppenTeilnehmer> findTeilnehmerByGroups(Integer groupID){
        List<GruppenTeilnehmer> all=gruppenTeilnehmerRepository.findAll();
        List<GruppenTeilnehmer> wanted=new ArrayList<>();
        for(GruppenTeilnehmer found:all){
            if(found.getGroupID().equals(groupID)){
                wanted.add(found);
            }
        }
        return wanted;
    }
    public List<GruppenTeilnehmer> findGroupsByParticipant(String particpant){
        List<GruppenTeilnehmer> all=gruppenTeilnehmerRepository.findAll();
        List<GruppenTeilnehmer> wanted=new ArrayList<>();
        for(GruppenTeilnehmer found:all){
            if(found.getParticipant().equals(particpant)){
                wanted.add(found);
            }
        }
        return wanted;
    }

    public void delete(Integer groupID, String email) {
        List<GruppenTeilnehmer> all=gruppenTeilnehmerRepository.findAll();
        for(GruppenTeilnehmer found:all){
            if(groupID.equals(found.getGroupID()) && email.equals(found.getParticipant())){
                gruppenTeilnehmerRepository.delete(found);
                return;
            }
        }



    }
}
