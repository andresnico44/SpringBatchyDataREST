package com.ejemplo.batchrest.batch.processor;

import com.ejemplo.batchrest.domain.Persona;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PersonaProcessor implements ItemProcessor<Persona, Persona> {

    @Override
    public Persona process(Persona persona) throws Exception {
        // Ejemplo: Filtrar menores de edad
        if (persona.getEdad() < 18) return null;

        // Transformar nombre a mayúsculas
        persona.setNombre(persona.getNombre().toUpperCase());
        return persona;
    }
}