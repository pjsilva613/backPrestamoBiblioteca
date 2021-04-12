package com.ceiba.biblioteca.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tbl_prestamo")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Prestamo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El codigo isbn no puede ser vacio")
    @Size(max = 10, message = "El tamaño maximo es de 10")
    @Column(name = "isbn", nullable = false)
    private String isbn;

    @NotEmpty(message = "El identificacionUsuario no puede ser vacio")
    @Size(max = 10, message = "El tamaño maximo es de 10")
    @Column(name = "identificacion_usuario", nullable = false)
    private String identificacionUsuario;

    @Positive(message = "El idTipoUsuario debe ser mayor a 0")
    @Column(name = "id_tipo_usuario", nullable = false)
    private Integer tipoUsuario;

    private String estado;

    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDateTime fechaPrestamo;

    @Column(name = "fecha_devolucion", nullable = false)
    private LocalDateTime fechaDevolucion;

    @Column(name = "fecha_Maxima_Devolucion", nullable = false)
    private String fechaMaximaDevolucion;

}
