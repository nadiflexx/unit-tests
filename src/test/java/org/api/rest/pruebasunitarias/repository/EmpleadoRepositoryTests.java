package org.api.rest.pruebasunitarias.repository;

import org.api.rest.pruebasunitarias.model.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmpleadoRepositoryTests {
    //Usaremos la metogologia BDD
    // given - condición previa
    // when - acción
    // then - resultado esperado
    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Empleado empleado;
    @BeforeEach
    void setUp() {
         empleado = Empleado.builder()
                .nombre("Joaquín")
                .apellido("Lopez Diaz")
                .email("testing@gmail.com")
                .build();
    }

    @DisplayName("Test para guardar un empleado")
    @Test
    void testGuardarEmpleado() {
        //Given
        Empleado empleado = Empleado.builder()
                .nombre("Joaquín")
                .apellido("Lopez Diaz")
                .email("testing@gmail.com")
                .build();
        //When
        Empleado empleadoGuardado = empleadoRepository.save(empleado);

        //Then
        assertThat(empleadoGuardado).isNotNull();
        assertThat(empleadoGuardado.getId()).isGreaterThan(0);
    }


    @DisplayName("Test para listar empleados")
    @Test
    void testListarEmpleados() {
        //Given
        Empleado empleadoCreado = Empleado.builder()
                .nombre("Joaquín")
                .apellido("Lopez Diaz")
                .email("testing@gmail.com")
                .build();
        empleadoRepository.save(empleadoCreado);
        empleadoRepository.save(empleado);

        //When
        List<Empleado> listaEmpleados = empleadoRepository.findAll();

        //Then
        assertThat(listaEmpleados).isNotNull();
        assertThat(listaEmpleados.size()).isGreaterThanOrEqualTo(2);
    }

    @DisplayName("Test para obtener empleado por ID")
    @Test
    void testEmpleadoById() {
        //Given
        empleadoRepository.save(empleado);

        //When
        Empleado empleadoObtenido = empleadoRepository.findById(empleado.getId()).orElse(null);

        //Then
        assertThat(empleadoObtenido).isNotNull();
        assertThat(empleadoObtenido.getApellido()).isEqualTo("Lopez Diaz");
    }

    @DisplayName("Test para actualizar empleado por ID")
    @Test
    void testUpdateEmpleadoById() {
        //Given
        empleadoRepository.save(empleado);

        //When
        Empleado empleadoObtenido = empleadoRepository.findById(empleado.getId()).orElse(null);

        //Then
        assertThat(empleadoObtenido).isNotNull();

        //When
            empleadoObtenido.setEmail("lopez@hotmail.com");
            empleadoObtenido.setNombre("Pedro");
            empleadoObtenido.setApellido("Martinez Arias");

        //Then
        assertThat(empleadoObtenido.getNombre()).isEqualTo("Pedro");
        assertThat(empleadoObtenido.getApellido()).isEqualTo("Martinez Arias");
        assertThat(empleadoObtenido.getEmail()).isEqualTo("lopez@hotmail.com");

    }

    @DisplayName("Test para eliminar empleado por ID")
    @Test
    void testDeleteEmpleadoById() {
        //Given
        empleadoRepository.save(empleado);

        //When
        empleadoRepository.deleteById(empleado.getId());
        Empleado empleadoObtenido = empleadoRepository.findById(empleado.getId()).orElse(null);

        //Then
        assertThat(empleadoObtenido).isNull();
    }
}
