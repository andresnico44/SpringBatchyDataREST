package com.ejemplo.batchrest.repository;

import com.ejemplo.batchrest.domain.Persona;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PersonaRepository extends CrudRepository<Persona, Long> {

}