package org.api.rest.pruebasunitarias.service;

import org.api.rest.pruebasunitarias.model.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {

    Empleado saveEmpleado(Empleado empleado);

    List<Empleado> getAllEmpleados();

    Optional<Empleado> getEmpleadoById(long id);

    Empleado updateEmpleado(Empleado empleado);

    void deleteEmpleado(long id);
}
