package org.fcuevas.mockito.servicio;

import org.fcuevas.mockito.modelo.dao.ExamenDao;
import org.fcuevas.mockito.modelo.dao.IExamenDao;
import org.fcuevas.mockito.modelo.dao.IPreguntasDao;
import org.fcuevas.mockito.modelo.dto.Examen;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/*
Mockito permite crear objetos simulados ("mocks") que imitan el comportamiento de objetos reales,
sin necesidad de usarlos directamente.

Usas Mockito cuando:
- Tu clase depende de otras clases o servicios que no quieres probar directamente
- Quieres simular errores o respuestas específicas de una dependencia
- Necesitas verificar si una acción (como enviar un correo) ocurrió

Ejemplo de aplicación:
En una aplicación empresarial con capas (controlador → servicio → repositorio), puedes usar Mockito
para probar la lógica del servicio, simulando el comportamiento del repositorio sin acceder a la base
de datos real.
*/
class ExamenServicioTest {

    private IExamenDao examenDao;
    private IPreguntasDao preguntasDao;
    private IExamenServicio examenServicio;

    /*
    El siguiente bloque de código permite inyectar los objetos en vez de iniciarlizarlos manualmente
    en el método inicializacion(). Para esto se usan anotaciones, en el caso de los 2 primeros objetos
    se usa la anotación @Mock que automáticamente inyectará ambos con el mock(). En el caso del último
    objeto, como necesita los 2 objetos anteriores para ser construído, se anota con @InjectMocks para
    que automáticamente inyecte los 2 mocks anteriores. Luego solo queda utilizar el método
    "openMocks(this);" en el método que inicializa cada prueba "inicializacion()".

    @Mock
    IExamenDao examenDao;

    @Mock
    IPreguntasDao preguntasDao;

    @InjectMocks
    ExamenServicio examenServicio;

    @BeforeEach
    void inicializacion(){
        MockitoAnnotations.openMocks(this);
    }

    Otra manera, aprovechando la extensión del POM llamada "<artifactId>mockito-junit-jupiter</artifactId>"
    es anotar lo siguiente justo al inicio de esta clase Java de prueba: @ExtendWith(MockitoExtension.class)
    y ahí no es necesario colocar nada en el método que inicializa cada prueba "inicializacion()".
    */

    @BeforeEach
    void inicializacion(){
        this.examenDao = Mockito.mock(IExamenDao.class);
        this.preguntasDao = Mockito.mock(IPreguntasDao.class);
        this.examenServicio = new ExamenServicio(this.examenDao,this.preguntasDao);
    }

    /*
    Acá vemos una prueba común y corriente de JUNIT5:
        @Test
        @DisplayName("Prueba - Encontrar examen por nombre JUNIT5")
        void encontrarExamenPorNombre() {
            Examen examen = examenServicio.encontrarExamenPorNombre("Matemáticas");
            assertNotNull(examen);
            assertEquals(5L,examen.getId());
            assertEquals("Matemáticas",examen.getNombre());
        }
    */

    @Test
    @DisplayName("Prueba - Encontrar examen por nombre MOCKITO")
    void pruebaEncontrarExamenPorNombreMockito() {
        Mockito.when(examenDao.encontrarTodos()).thenReturn(Datos.EXAMENES);
        Examen examen = examenServicio.encontrarExamenPorNombre("Matemáticas");

        assertNotNull(examen);
        assertEquals(5L,examen.getId());
        assertEquals("Matemáticas",examen.getNombre());
    }

    @Test
    @DisplayName("Prueba - Buscar preguntas de examen MOCKITO")
    void pruebaPreguntasExamenesMockito() {
        Mockito.when(examenDao.encontrarTodos()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntasDao.encontrarPreguntasExamenId(5L)).thenReturn(Datos.PREGUNTAS);

        Examen examen = examenServicio.encontrarExamenPorNombreConPreguntas("Matemáticas");
        assertAll(
                ()->{assertNotNull(examen);},
                ()->{assertEquals(5,examen.getPreguntas().size());},
                ()->{assertEquals("Matemáticas",examen.getNombre());},
                ()->{assertTrue(examen.getPreguntas().contains("Aritmética"));});
    }

    @Test
    @DisplayName("Prueba - Buscar preguntas de examen MOCKITO VERIFY")
    void pruebaPreguntasExamenesMockitoVerify() {
        Mockito.when(examenDao.encontrarTodos()).thenReturn(Datos.EXAMENES);
        Mockito.when(preguntasDao.encontrarPreguntasExamenId(5L)).thenReturn(Datos.PREGUNTAS);

        Examen examen = examenServicio.encontrarExamenPorNombreConPreguntas("Matemáticas");
        assertAll(
                ()->{assertNotNull(examen);},
                ()->{assertEquals(5,examen.getPreguntas().size());},
                ()->{assertEquals("Matemáticas",examen.getNombre());},
                ()->{assertTrue(examen.getPreguntas().contains("Aritmética"));});

        /*
        Mockito.verify(examenDao).encontrarTodos();
        ---
        Al ejecutar la prueba anterior, se le dice a Mockito que verifique la llamada a ese método
        "encontrarTodos()", y Mockito por defecto solamente valida que sea llamado 1 vez. Revisando el
        método en la clase ExamenServicio.java vemos que el método es invocado 2 veces en el método
        "encontrarExamenPorNombre()" por lo que se debe especificar a Mockito la cantidad de veces que
        se espera llamar al método en cuestión colocando otro parámetro al método "verify()" de lo
        contrario la prueba fallará:
        ---------------
        Mockito.verify(examenDao, Mockito.times(2)).encontrarTodos();
        */
        Mockito.verify(examenDao, Mockito.times(2)).encontrarTodos();
        Mockito.verify(preguntasDao).encontrarPreguntasExamenId(5L);
    }

    @Test
    @DisplayName("Prueba - Guardar un examen MOCKITO VERIFY")
    void pruebaGuardarExamen() {
        Mockito.when(examenDao.guardar(Mockito.any(Examen.class))).thenReturn(Datos.EXAMEN);
        Examen examen = examenDao.guardar(Datos.EXAMEN);
        assertAll(
                ()->{assertNotNull(examen.getId());},
                ()->{assertEquals(8L,examen.getId());},
                ()->{assertEquals("Física",examen.getNombre());}
        );
        Mockito.verify(examenDao).guardar(Mockito.any(Examen.class));
    }
}