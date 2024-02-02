package com.example.Spring2.Diskussion.GruppenTeilnehmer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GruppenTeilnehmerRepository extends JpaRepository<GruppenTeilnehmer, GruppenTeilnehmerID> {
}
