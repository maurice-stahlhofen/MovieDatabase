package com.example.Spring2.DiskussionsEinladungPackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("controller/diskussionseinladung")
public class DiskussionseinladungController {

    private DiskussionseinladungService diskussionseinladungService;

    @Autowired public DiskussionseinladungController(DiskussionseinladungService diskussionseinladungService){
        this.diskussionseinladungService = diskussionseinladungService;
    }

    @GetMapping
    public List<Diskussionseinladung> getFreundeseinladung(){
        return diskussionseinladungService.getDiskussionseinladung();
    }

    @PostMapping("add")
    public void addDiskussionseinladung(@RequestBody Diskussionseinladung diskussionseinladung){
        diskussionseinladungService.addDiskussionseinladung(diskussionseinladung);
    }

    @ResponseBody
    @RequestMapping(value = "delete/{id}",produces = "application/json",method ={RequestMethod.DELETE,RequestMethod.GET})
    public void declineDiskussionseinladung(@PathVariable("id") Integer id) {
        diskussionseinladungService.deleteByID(id);
    }

    @ResponseBody
    @PostMapping("delete")
    public void declineDiskussionseinladung(@RequestBody Diskussionseinladung diskussionseinladung){
        diskussionseinladungService.delete(diskussionseinladung);
    }
}
