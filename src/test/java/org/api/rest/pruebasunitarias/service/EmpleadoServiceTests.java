package org.api.rest.pruebasunitarias.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

import org.api.rest.pruebasunitarias.exception.DataNotFoundException;
import org.api.rest.pruebasunitarias.model.Empleado;
import org.api.rest.pruebasunitarias.repository.EmpleadoRepository;
import org.api.rest.pruebasunitarias.service.implementation.EmpleadoServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTests {
    //@Mock permite crear simulacros
    @Mock
    private EmpleadoRepository empleadoRepository;
    //@InjectMocks inyecta el Servicio dentro del Repositorio
    @InjectMocks
    private EmpleadoServiceImplementation empleadoService;

    private Empleado empleado;

    @BeforeEach
    void setUp() {
        empleado = Empleado.builder()
                .id(1L)
                .nombre("Joaquín")
                .apellido("Lopez Diaz")
                .email("testing@gmail.com")
                .build();
    }

    @DisplayName("Test para guardar un empleado")
    @Test
    void testGuardarEmpleado() {
        //Given
        doReturn(empleado).when(empleadoRepository).save(empleado);

        //When
        Empleado empleadoGuardado = empleadoService.saveEmpleado(empleado);

        //Then
        assertThat(empleadoGuardado).isNotNull();
    }

    @DisplayName("Test para guardar un empleado con Excepción")
    @Test
    void testGuardarEmpleadoExcepcion() {
        //Given
        doReturn(Optional.of(empleado)).when(empleadoRepository).findEmpleadoByEmail(empleado.getEmail());

        //When
        //Al ya existir existir este empleado en la BBDD, se lanza una excepción y el servicio intenta guardar
        //el empleado pero como ya existe no hará nada.
        assertThrows(DataNotFoundException.class, () -> empleadoService.saveEmpleado(empleado));

        //Then
        //Buscamos que verifique que NO se ha llamado al repositorio ni 1 vez para guardar ningun Empleado
        verify(empleadoRepository, never()).save(any(Empleado.class));
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
        given(empleadoRepository.findAll()).willReturn(List.of(empleado, empleadoCreado));

        //When
        List<Empleado> listaEmpleados = empleadoService.getAllEmpleados();

        //Then
        assertThat(listaEmpleados).isNotNull();
        assertThat(listaEmpleados.size()).isEqualTo(2);
    }

    @DisplayName("Test para devolver lista vacía")
    @Test
    void testListaVaciaEmpleados() {
        //Given
        given(empleadoRepository.findAll()).willReturn(Collections.emptyList());

        //When
        List<Empleado> listaEmpleados = empleadoService.getAllEmpleados();

        //Then
        assertThat(listaEmpleados.size()).isEqualTo(0);
        assertThat(listaEmpleados).isEmpty();
    }

    @DisplayName("Test para obtener empleado por ID")
    @Test
    void testEmpleadoById() {
        //Given
        doReturn(Optional.of(empleado)).when(empleadoRepository).findById(empleado.getId());

        //When
        Empleado empleadoOptional = empleadoService.getEmpleadoById(empleado.getId()).get();

        //Then
        assertThat(empleadoOptional).isNotNull();
        assertThat(empleadoOptional.getId()).isEqualTo(1L);
    }

    @DisplayName("Test para actualizar empleado")
    @Test
    void testUpdateEmpleado() {
        //Given
        doReturn(empleado).when(empleadoRepository).save(empleado);
        empleado.setNombre("Marcos");
        empleado.setApellido("Perez Gonzalez");
        empleado.setEmail("marcos@outlook.com");

        //When
        Empleado empleadoOptional = empleadoService.updateEmpleado(empleado);

        //Then
        assertThat(empleadoOptional).isNotNull();
        assertThat(empleadoOptional.getNombre()).isEqualTo("Marcos");
        assertThat(empleadoOptional.getApellido()).isEqualTo("Perez Gonzalez");
        assertThat(empleadoOptional.getEmail()).isEqualTo("marcos@outlook.com");
    }

    @DisplayName("Test para eliminar empleado")
    @Test
    void testDeleteEmpleado() {
        //Given
        long idEmpleado = empleado.getId();
        lenient().when(empleadoRepository.findById(empleado.getId())).thenReturn(Optional.ofNullable(empleado));
        lenient().doNothing().when(empleadoRepository).deleteById(idEmpleado);

        //When
        empleadoService.deleteEmpleado(idEmpleado);

        //Then
        //Buscamos que verifique que se ha llamado al repositorio 1 vez para ejecutar la función deleteById() con el idEmpleado
        verify(empleadoRepository, times(1)).findById(idEmpleado);
        verify(empleadoRepository, times(1)).deleteById(idEmpleado);
    }
}
