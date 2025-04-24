package com.ejemplo.batchrest.batch.processor;

import com.ejemplo.batchrest.domain.Persona;
import com.ejemplo.batchrest.domain.PersonaCsv;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PersonaProcessor implements ItemProcessor<PersonaCsv, Persona> {

    @Override
    public Persona process(PersonaCsv personaCsv) throws Exception {
        if (personaCsv.getEdad() == null || personaCsv.getNombre() == null) {
            return null;
        }

        try {
            int edad = Integer.parseInt(personaCsv.getEdad());
            if (edad < 18) {
                return null;
            }

            Persona persona = new Persona();
            persona.setNombre(personaCsv.getNombre().toUpperCase());
            persona.setEdad(edad);
            return persona;

        } catch (NumberFormatException e) {
            // Manejo de error: si la edad no es un número válido
            return null; 
        }
    }
}