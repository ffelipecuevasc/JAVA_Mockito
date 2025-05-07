package org.fcuevas.mockito.modelo.dao;

import java.util.List;

public interface IPreguntasDao {

    public void guardarVarias(List<String> preguntas);
    public List<String> encontrarPreguntasExamenId(Long id);
}
