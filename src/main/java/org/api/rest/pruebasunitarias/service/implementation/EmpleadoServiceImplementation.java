package org.api.rest.pruebasunitarias.service.implementation;

import org.api.rest.pruebasunitarias.exception.DataNotFoundException;
import org.api.rest.pruebasunitarias.model.Empleado;
import org.api.rest.pruebasunitarias.repository.EmpleadoRepository;
import org.api.rest.pruebasunitarias.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImplementation implements EmpleadoService {

    @Autowired
    private EmpleadoRepository repository;

    @Override
    public Empleado saveEmpleado(Empleado empleado) {
        Optional<Empleado> empleadoOptional = repository.findEmpleadoByEmail(empleado.getEmail());

        if(empleadoOptional.isPresent()) {
            throw new DataNotFoundException("El empleado con este mail ya existe: " + empleado.getEmail());
        }
        return repository.save(empleado);
    }

    @Override
    public List<Empleado> getAllEmpleados() {
        return repository.findAll();
    }

    @Override
    public Optional<Empleado> getEmpleadoById(long id) {
        return repository.findById(id);
    }

    @Override
    public Empleado updateEmpleado(Empleado empleado) {
        return repository.save(empleado);
    }

    @Override
    public void deleteEmpleado(long id) {
        Optional<Empleado> empleadoOptional = repository.findById(id);

        if(empleadoOptional.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new DataNotFoundException("El empleado que buscas no existe");
        }
    }
}
