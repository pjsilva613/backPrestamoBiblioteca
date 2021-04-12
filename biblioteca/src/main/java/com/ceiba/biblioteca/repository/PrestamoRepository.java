package com.ceiba.biblioteca.repository;

import com.ceiba.biblioteca.entity.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    public Prestamo findByIdentificacionUsuarioAndEstado(String identificacionUsuario, String estado);
}
