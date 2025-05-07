package org.fcuevas.mockito.modelo.dao;

import org.fcuevas.mockito.modelo.dto.Examen;

import java.util.Arrays;
import java.util.List;

public class ExamenDao implements IExamenDao{

    @Override
    public Examen guardar(Examen examen) {
        return null;
    }

    @Override
    public List<Examen> encontrarTodos() {
        return Arrays.asList(new Examen(5L,"Matemáticas"),
                new Examen(6L, "Lenguaje"),
                new Examen(7L,"Historia"));
    }
}
