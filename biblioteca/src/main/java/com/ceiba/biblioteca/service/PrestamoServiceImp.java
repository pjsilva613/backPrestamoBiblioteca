package com.ceiba.biblioteca.service;

import com.ceiba.biblioteca.entity.Prestamo;
import com.ceiba.biblioteca.repository.PrestamoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service

public class PrestamoServiceImp implements PrestamoServiceInt{
    private static final String ACTIVO="ACTIVO";
    private static final String MSG_USUARIO_IDENTIFICACION="El usuario con identificación ";
    private static final String MSG_INVITADO_PRESTAMO_ACTIVO=" ya tiene un libro prestado por lo cual no se le puede realizar otro préstamo";
    private static final String MSG_USUARIO_NO_PERMITIDO="Tipo de usuario no permitido en la biblioteca";
    LocalDateTime localDateTime= LocalDateTime.now();

    public PrestamoServiceImp(PrestamoRepository prestamoRepository) {
        this.prestamoRepository = prestamoRepository;
    }

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Override
    public List<Prestamo> findAllPrestamos() {
        return prestamoRepository.findAll();
    }

    @Override
    public Prestamo createPrestamo(Prestamo prestamo) {
        DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("dd/MM/yyyy");
        prestamo.setEstado(ACTIVO);
        prestamo.setFechaPrestamo(localDateTime);
        prestamo.setFechaDevolucion(obtenerFechaDevolucion(obtenerCantidadDias(prestamo.getTipoUsuario())));
        prestamo.setFechaMaximaDevolucion(dateTimeFormatter.format(prestamo.getFechaDevolucion()));
        Prestamo prestamoDb = prestamoRepository.findByIdentificacionUsuarioAndEstado(prestamo.getIdentificacionUsuario(), prestamo.getEstado());
        System.out.println("trae info de base de datos:::::::::::::::"+prestamoDb);
        if ((prestamo.getTipoUsuario()==3) && (null!=prestamoDb) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MSG_USUARIO_IDENTIFICACION+ prestamo.getIdentificacionUsuario()+ MSG_INVITADO_PRESTAMO_ACTIVO);
        }
        return prestamoRepository.save(prestamo);
    }

    public Prestamo findByIdPrestamo(Long id){
        return prestamoRepository.findById(id).orElse(null);
    }

    public int obtenerCantidadDias(Integer tipoAfiliacion){

        switch (tipoAfiliacion){
            case 1:return 10;
            case 2:return 8;
            case 3:return 7;
            default: throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MSG_USUARIO_NO_PERMITIDO);
        }
    }

    public LocalDateTime obtenerFechaDevolucion(int cantidadDias){
        LocalDateTime  localDateTime= LocalDateTime.now();
        while (cantidadDias-1 > 0){
            if ((!localDateTime.getDayOfWeek().equals(DayOfWeek.SUNDAY)) && (!localDateTime.getDayOfWeek().equals(DayOfWeek.SATURDAY))) {
                cantidadDias-=1;
            }
            localDateTime = localDateTime.plusDays(1);
        }
        return localDateTime;
    }
}