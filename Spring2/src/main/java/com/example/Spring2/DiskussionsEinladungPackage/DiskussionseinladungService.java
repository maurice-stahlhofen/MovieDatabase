package com.example.Spring2.DiskussionsEinladungPackage;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiskussionseinladungService {

    private DiskussionseinladungRepository diskussionseinladungRepository;

    public DiskussionseinladungService(DiskussionseinladungRepository diskussionseinladungRepository){
        this.diskussionseinladungRepository = diskussionseinladungRepository;
    }

    public List<Diskussionseinladung> getDiskussionseinladung(){
      return diskussionseinladungRepository.findAll();
    }

    public void addDiskussionseinladung(Diskussionseinladung diskussionseinladung){
        diskussionseinladungRepository.save(diskussionseinladung);
    }

    public void delete(Diskussionseinladung diskussionseinladung) {
        diskussionseinladungRepository.delete(diskussionseinladung);
    }

    public void deleteByID(Integer id){
        diskussionseinladungRepository.deleteById(id);
    }
}
