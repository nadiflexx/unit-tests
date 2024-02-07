package org.api.rest.pruebasunitarias.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.api.rest.pruebasunitarias.model.Empleado;
import org.api.rest.pruebasunitarias.service.EmpleadoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import java.util.List;
import java.util.Optional;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;

@WebMvcTest
public class EmpleadoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean //Objeto simulado
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper mapper;

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
    void testGuardarEmpleado() throws Exception {
        //Given
        given(empleadoService.saveEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));
        //When
        ResultActions respone = mockMvc.perform(post("/empleados/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(empleado)));
        //Then
        respone.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleado.getEmail())));
    }

    @DisplayName("Test para listar un empleado")
    @Test
    void testListarEmpleado() throws Exception {
        List<Empleado> listaEmpleados = empleadoService.getAllEmpleados();
        //Given
        listaEmpleados.add(empleado);
        listaEmpleados.add(Empleado.builder().nombre("Fede").apellido("Torres Suarez").email("fede@hotmail.es").build());
        listaEmpleados.add(Empleado.builder().nombre("Marco").apellido("Jimenez Arias").email("marco@outlook.com").build());

        given(empleadoService.getAllEmpleados()).willReturn(listaEmpleados);

        //When
        ResultActions respone = mockMvc.perform(get("/empleados"));

        //Then
        respone.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(listaEmpleados.size())));
    }

    @DisplayName("Test para listar un empleado por Id")
    @Test
    void testEmpleadoById() throws Exception {
        //Given
        given(empleadoService.getEmpleadoById(empleado.getId())).willReturn(Optional.of(empleado));

        //When
        ResultActions respone = mockMvc.perform(get("/empleados/{id}", empleado.getId()));

        //Then
        respone.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleado.getApellido())))
                .andExpect(jsonPath("$.email", is(empleado.getEmail())));
    }

    @DisplayName("Test para no encontrar un empleado por Id")
    @Test
    void testEmpleadoByIdNotFound() throws Exception {
        //Given
        given(empleadoService.getEmpleadoById(empleado.getId())).willReturn(Optional.empty());

        //When
        ResultActions respone = mockMvc.perform(get("/empleados/{id}", empleado.getId()));

        //Then
        respone.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test para actualizar un empleado")
    @Test
    void testUpdateEmpleado() throws Exception {
        //Given
        Empleado empleadoUpdate = (Empleado.builder()
                .nombre("Marco")
                .apellido("Jimenez Arias")
                .email("marco@outlook.com")
                .build());

        given(empleadoService.getEmpleadoById(empleado.getId())).willReturn(Optional.ofNullable(empleado));
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //When
        ResultActions respone = mockMvc.perform(put("/empleados/{id}", empleado.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(empleadoUpdate)));

        //Then
        respone.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is(empleadoUpdate.getNombre())))
                .andExpect(jsonPath("$.apellido", is(empleadoUpdate.getApellido())))
                .andExpect(jsonPath("$.email", is(empleadoUpdate.getEmail())));
    }

    @DisplayName("Test para actualizar un empleado no encontrado")
    @Test
    void testUpdateEmpleadoNotFound() throws Exception {
        //Given
        Empleado empleadoUpdate = (Empleado.builder()
                .nombre("Marco")
                .apellido("Jimenez Arias")
                .email("marco@outlook.com")
                .build());

        given(empleadoService.getEmpleadoById(empleado.getId())).willReturn(Optional.empty());
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //When
        ResultActions respone = mockMvc.perform(put("/empleados/{id}", empleado.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(empleadoUpdate)));

        //Then
        respone.andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test para eliminar empleado")
    @Test
    void testDeleteEmpleado() throws Exception {
        //Given
        long idEmpleado = empleado.getId();
        given(empleadoService.getEmpleadoById(idEmpleado)).willReturn(Optional.ofNullable(empleado));
        willDoNothing().given(empleadoService).deleteEmpleado(idEmpleado);

        //When
        ResultActions respone = mockMvc.perform(delete("/empleados/{id}", idEmpleado));

        //Then
        respone.andDo(print())
                .andExpect(status().isOk());
    }
}
