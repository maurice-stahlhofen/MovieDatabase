package com.example.Spring2.repository;

import com.example.Spring2.entities.Freundesliste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FreundeslisteRepository extends JpaRepository<Freundesliste, Integer> {

    
}
