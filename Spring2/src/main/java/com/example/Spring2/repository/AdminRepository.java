package com.example.Spring2.repository;

import com.example.Spring2.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

/*
Controller, Annotationen bei den Entitäten, Repositories
sind an diesen Aufbau angelehnt:
https://www.baeldung.com/rest-http-put-vs-post
 */

public interface AdminRepository extends JpaRepository<Admin, String> {

}
