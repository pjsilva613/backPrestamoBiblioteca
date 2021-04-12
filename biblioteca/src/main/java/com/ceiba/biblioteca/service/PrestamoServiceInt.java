package com.ceiba.biblioteca.service;

import com.ceiba.biblioteca.entity.Prestamo;

import java.util.List;

public interface PrestamoServiceInt {

    public List<Prestamo> findAllPrestamos();
    public Prestamo createPrestamo(Prestamo prestamo);
    public Prestamo findByIdPrestamo(Long id);
}
