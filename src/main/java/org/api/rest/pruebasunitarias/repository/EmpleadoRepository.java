package org.api.rest.pruebasunitarias.repository;


import org.api.rest.pruebasunitarias.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
	@Query(value = "SELECT * FROM empleados WHERE email=:email", nativeQuery = true)
	Optional<Empleado> findEmpleadoByEmail(@Param("email") String email);
}