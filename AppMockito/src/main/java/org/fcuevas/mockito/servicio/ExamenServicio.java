package org.fcuevas.mockito.servicio;

import org.fcuevas.mockito.modelo.dao.IExamenDao;
import org.fcuevas.mockito.modelo.dao.IPreguntasDao;
import org.fcuevas.mockito.modelo.dto.Examen;

import java.util.List;
import java.util.Optional;

public class ExamenServicio implements IExamenServicio{

    private IExamenDao examenDao;
    private IPreguntasDao preguntasDao;

    public ExamenServicio(IExamenDao examenDao, IPreguntasDao preguntasDao){
        this.examenDao = examenDao;
        this.preguntasDao = preguntasDao;
    }

    @Override
    public Examen encontrarExamenPorNombre(String nombre) {
        if(!examenDao.encontrarTodos().stream().filter(e -> e.getNombre().contains(nombre)).findFirst().isPresent()){
            return null;
        }
        return examenDao.encontrarTodos().stream().filter(e -> e.getNombre().contains(nombre)).findFirst().get();
    }

    @Override
    public Examen encontrarExamenPorNombreConPreguntas(String nombre) {
        Examen examen = encontrarExamenPorNombre(nombre);
        if(examen != null){
            List<String> preguntas = preguntasDao.encontrarPreguntasExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen guardarExamen(Examen examen) {
        if(!examen.getPreguntas().isEmpty()) preguntasDao.guardarVarias(examen.getPreguntas());
        return examenDao.guardar(examen);
    }
}
