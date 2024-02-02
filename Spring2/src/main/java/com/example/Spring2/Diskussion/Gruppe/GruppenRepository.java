package com.example.Spring2.Diskussion.Gruppe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GruppenRepository extends JpaRepository<Gruppe, Integer> {
}
