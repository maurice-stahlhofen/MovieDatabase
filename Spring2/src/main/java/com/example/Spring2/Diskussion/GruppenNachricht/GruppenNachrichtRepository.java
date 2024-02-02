package com.example.Spring2.Diskussion.GruppenNachricht;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GruppenNachrichtRepository extends JpaRepository<GruppenNachricht, Integer> {
}
