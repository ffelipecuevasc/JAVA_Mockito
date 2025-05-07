package org.fcuevas.mockito.modelo.dao;

import org.fcuevas.mockito.modelo.dto.Examen;

import java.util.List;

public interface IExamenDao {

    public Examen guardar(Examen examen);
    public List<Examen> encontrarTodos();
}
