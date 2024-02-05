package org.api.rest.pruebasunitarias.controller;

import org.api.rest.pruebasunitarias.model.Empleado;
import org.api.rest.pruebasunitarias.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService service;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Empleado saveEmplead(@RequestBody Empleado empleado) {
        return service.saveEmpleado(empleado);
    }

    @GetMapping
    public List<Empleado> getAllEmpleados() {
        return service.getAllEmpleados();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> getEmpleadoById(@PathVariable long id) {
        return service.getEmpleadoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> updateEmpleado(@PathVariable long id, @RequestBody Empleado empleado) {
        return service.getEmpleadoById(id)
                .map(empleadoDeseado -> {
                    empleadoDeseado.setNombre(empleado.getNombre());
                    empleadoDeseado.setApellido(empleado.getApellido());
                    empleadoDeseado.setEmail(empleado.getEmail());

                    Empleado empleadoUpdate = service.updateEmpleado(empleadoDeseado);
                    return new ResponseEntity<>(empleadoUpdate, HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmpleadoById(@PathVariable long id) {
        service.deleteEmpleado(id);
        return new ResponseEntity<>("Empleado eliminado correctamente", HttpStatus.OK);
    }
}
