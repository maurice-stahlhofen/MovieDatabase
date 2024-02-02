package com.example.Spring2.service;

import com.example.Spring2.entities.Admin;
import com.example.Spring2.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository){
        this.adminRepository=adminRepository;
    }

    public List<Admin> getAdmin(){
        return adminRepository.findAll();
    }

    public void addAdmin(Admin admin){
        adminRepository.save(admin);
    }

    public void deleteAdmin(Admin admin){
        if(adminRepository.findById(admin.getEmail()).toString().isEmpty()){
            System.out.println("Admin existiert nicht.");
        }
        else{
            adminRepository.deleteById(admin.getEmail());
            System.out.println("Admin wurde gelöscht!");
        }
    }

    public void aendereAdmin(Admin admin){
        if(adminRepository.findById(admin.getEmail()).toString().isEmpty()){
            System.out.println("Admin wurde noch nicht angelegt.");
        }else{
            adminRepository.save(admin);
        }
    }

    public List<Admin> getAdminbyEmail(String email) {
        List<Admin>alle=adminRepository.findAll();
        List<Admin> gesucht=new ArrayList<>();
        for(Admin admin:alle){
            if(admin.getEmail().equals(email)){
                gesucht.add(admin);
            }
        }
        return gesucht;
    }


}
