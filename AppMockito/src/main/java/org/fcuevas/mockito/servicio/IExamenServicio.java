package org.fcuevas.mockito.servicio;

import org.fcuevas.mockito.modelo.dto.Examen;

public interface IExamenServicio {

    public Examen encontrarExamenPorNombre(String nombre);

    public Examen encontrarExamenPorNombreConPreguntas(String nombre);

    public Examen guardarExamen(Examen examen);
}
