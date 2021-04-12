package com.ceiba.biblioteca;

import com.ceiba.biblioteca.entity.Prestamo;
import com.ceiba.biblioteca.repository.PrestamoRepository;
import com.ceiba.biblioteca.service.PrestamoServiceImp;
import com.ceiba.biblioteca.service.PrestamoServiceInt;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class PrestamoServiceMockTest {

    public static final int USUARIO_AFILIADO = 1;
    public static final int USUARIO_EMPLEADO = 2;
    public static final int USUARIO_INVITADO = 3;
    public static final int USUARIO_DESCONOCIDO = 5;
    @Mock
    private PrestamoRepository prestamoRepository;

    private PrestamoServiceInt prestamoServiceInt;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        prestamoServiceInt =  new PrestamoServiceImp( prestamoRepository);
        LocalDateTime localDateTime = LocalDateTime.now();
        Prestamo prestamo = Prestamo.builder()
                .id(5L)
                .isbn("REPO")
                .identificacionUsuario("1")
                .tipoUsuario(USUARIO_INVITADO)
                .estado("ACTIVO")
                .fechaPrestamo(localDateTime)
                .fechaDevolucion(localDateTime.plusDays(12))
                .fechaMaximaDevolucion("23/04/2021")
                .build();

        Mockito.when(prestamoRepository.findById(5L))
                .thenReturn(Optional.of(prestamo));
        Mockito.when(prestamoRepository.save(prestamo)).thenReturn(prestamo);

    }

    @Test
    public void whenValidGetID_ThenReturnPrestamo(){
        Prestamo prestamoDB = prestamoServiceInt.findByIdPrestamo(5L);
        Assertions.assertThat(prestamoDB.getIdentificacionUsuario()).isEqualTo("1");

    }

}
