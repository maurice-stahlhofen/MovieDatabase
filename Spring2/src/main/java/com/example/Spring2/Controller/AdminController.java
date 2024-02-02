package com.example.Spring2.Controller;

import com.example.Spring2.entities.Admin;
import com.example.Spring2.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
Controller, Annotationen bei den Entitäten, Repositories
sind an diesen Aufbau angelehnt:
https://www.baeldung.com/rest-http-put-vs-post
 */


@RestController
@RequestMapping(value="controller/admin")

public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public List<Admin> getadmin(){
        return adminService.getAdmin();
    }

    @PostMapping("add")
    public void addadmin(@RequestBody Admin admin){
        adminService.addAdmin(admin);
    }

    @PutMapping(value="put")
    public void aendereadmin(@RequestBody Admin admin){
        adminService.aendereAdmin(admin);
    }

    @DeleteMapping("delete")
    public void deleteadmin(@RequestBody Admin admin){
        adminService.deleteAdmin(admin);
    }

    @GetMapping("email")
    public List<Admin> getAdminbyEmail(@PathVariable("email") String email){
        return adminService.getAdminbyEmail(email);
    }

    @GetMapping(value="email/{email}")
    public List<Admin> getAdminbyId(@PathVariable("email")String email){
        return adminService.getAdminbyEmail(email);
    }
}

