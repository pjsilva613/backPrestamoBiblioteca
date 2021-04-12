package com.ceiba.biblioteca;

import com.ceiba.biblioteca.entity.Prestamo;
import com.ceiba.biblioteca.repository.PrestamoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class PrestamoRepositoryMockTest {

    public static final int USUARIO_AFILIADO = 1;
    public static final int USUARIO_EMPLEADO = 2;
    public static final int USUARIO_INVITADO = 3;
    public static final int USUARIO_DESCONOCIDO = 5;

    @Autowired
    PrestamoRepository prestamoRepository;

    @Test
    public void whenFindAllPrestamos_ThenReturnAListPrestamos(){
        LocalDateTime localDateTime = LocalDateTime.now();
        Prestamo prestamo = Prestamo.builder()
                .isbn("REPO")
                .identificacionUsuario("1")
                .tipoUsuario(USUARIO_INVITADO)
                .estado("ACTIVO")
                .fechaPrestamo(localDateTime)
                .fechaDevolucion(localDateTime.plusDays(12))
                .fechaMaximaDevolucion("23/04/2021")
                .build();

        prestamoRepository.save(prestamo);

        List<Prestamo> prestamosList = prestamoRepository.findAll();

        Assertions.assertThat(prestamosList.size()).isEqualTo(3);
    }



}
