package com.ceiba.biblioteca;

import com.ceiba.biblioteca.controller.PrestamoControlador;
import com.ceiba.biblioteca.entity.Prestamo;
import com.ceiba.biblioteca.service.PrestamoServiceInt;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.core.Is.is;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class PrestamoTests {

    public static final int USUARIO_AFILIADO = 1;
    public static final int USUARIO_EMPLEADO = 2;
    public static final int USUARIO_INVITADO = 3;
    public static final int USUARIO_DESCONOCIDO = 5;


    @Autowired
    private PrestamoServiceInt prestamoServiceInt;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void prestamoLibroUsuarioAfiliadoDeberiaAlmacenarCorrectamenteYCalcularFechaDeDevolucion() throws Exception {

        Prestamo prestamo= Prestamo.builder()
                .isbn("ASDA7884")
                .identificacionUsuario("974148")
                .tipoUsuario(USUARIO_AFILIADO).build();

        MvcResult resultadoLibroPrestado = mvc.perform(
                MockMvcRequestBuilders.post("/prestamo")
                        .contentType(MediaType.APPLICATION_JSON)
                        //.content(objectMapper.writeValueAsString(new SolicitudPrestarLibroTest("ASDA7884", "974148", USUARIO_AFILIADO))))
                        .content(objectMapper.writeValueAsString( prestamoServiceInt.createPrestamo(prestamo))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fechaMaximaDevolucion").exists())
                .andReturn();


        ResultadoPrestarTest resultadoPrestamo = objectMapper.readValue(resultadoLibroPrestado.getResponse().getContentAsString(), ResultadoPrestarTest.class);

        LocalDate fechaPrestamo = LocalDate.now();
        fechaPrestamo = addDaysSkippingWeekends(fechaPrestamo, 10);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        mvc.perform(MockMvcRequestBuilders
                .get("/prestamo/" + resultadoPrestamo.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fechaMaximaDevolucion", is(fechaPrestamo.format(formato))))
                .andExpect(jsonPath("$.isbn", is("ASDA7884")))
                .andExpect(jsonPath("$.identificacionUsuario", is("974148")))
                .andExpect(jsonPath("$.tipoUsuario", is(USUARIO_AFILIADO)));


    }

    @Test
    public void prestamoLibroUsuarioEmpleadoDeberiaAlmacenarCorrectamenteYCalcularFechaDeDevolucion() throws Exception {

        Prestamo prestamo= Prestamo.builder()
                .isbn("AWQ489")
                .identificacionUsuario("7481545")
                .tipoUsuario(USUARIO_EMPLEADO).build();

        MvcResult resultadoLibroPrestado = mvc.perform(MockMvcRequestBuilders
                .post("/prestamo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prestamoServiceInt.createPrestamo(prestamo))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fechaMaximaDevolucion").exists())
                .andReturn();

        ResultadoPrestarTest resultadoPrestamo = objectMapper.readValue(resultadoLibroPrestado.getResponse().getContentAsString(), ResultadoPrestarTest.class);

        LocalDate fechaPrestamo = LocalDate.now();
        fechaPrestamo = addDaysSkippingWeekends(fechaPrestamo, 8);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        mvc.perform(MockMvcRequestBuilders
                .get("/prestamo/" + resultadoPrestamo.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fechaMaximaDevolucion", is(fechaPrestamo.format(formato))))
                .andExpect(jsonPath("$.isbn", is("AWQ489")))
                .andExpect(jsonPath("$.identificacionUsuario", is("7481545")))
                .andExpect(jsonPath("$.tipoUsuario", is(USUARIO_EMPLEADO)));


    }

    @Test
    public void prestamoLibroUsuarioInvitadoDeberiaAlmacenarCorrectamenteYCalcularFechaDeDevolucion() throws Exception {

        Prestamo prestamo= Prestamo.builder()
                .isbn("EQWQW")
                .identificacionUsuario("14012609")
                .tipoUsuario(USUARIO_INVITADO)
                .build();

        MvcResult resultadoLibroPrestado = mvc.perform(MockMvcRequestBuilders
                .post("/prestamo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prestamoServiceInt.createPrestamo(prestamo))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fechaMaximaDevolucion").exists())
                .andReturn();

        ResultadoPrestarTest resultadoPrestamo = objectMapper.readValue(resultadoLibroPrestado.getResponse().getContentAsString(), ResultadoPrestarTest.class);

        LocalDate fechaPrestamo = LocalDate.now();
        fechaPrestamo = addDaysSkippingWeekends(fechaPrestamo, 7);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        mvc.perform(MockMvcRequestBuilders
                .get("/prestamo/" + resultadoPrestamo.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fechaMaximaDevolucion", is(fechaPrestamo.format(formato))))
                .andExpect(jsonPath("$.isbn", is("EQWQW")))
                .andExpect(jsonPath("$.identificacionUsuario", is("14012609")))
                .andExpect(jsonPath("$.tipoUsuario", is(USUARIO_INVITADO)));


    }

    @Test
    public void usuarioInvitadoTratandoDePrestarUnSegundoLibroDeberiaRetornarExcepcion() throws Exception {

        Prestamo prestamo= Prestamo.builder()
                .isbn("EQWQW8545")
                .identificacionUsuario("1111111111")
                .tipoUsuario(USUARIO_INVITADO)
                .build();

        mvc.perform(MockMvcRequestBuilders
                .post("/prestamo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prestamoServiceInt.createPrestamo(prestamo))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fechaMaximaDevolucion").exists());

        mvc.perform(MockMvcRequestBuilders
                .post("/prestamo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prestamoServiceInt.createPrestamo(prestamo))))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.mensaje", is("El usuario con identificaci??n 1111111111 ya tiene un libro prestado por lo cual no se le puede realizar otro pr??stamo")));

    }

    @Test
    public void usuarioNoInvitadoTratandoDePrestarUnSegundoLibroDeberiaPrestarloCorrectamente() throws Exception {


        Prestamo prestamo= Prestamo.builder()
                .isbn("EQWQW8545")
                .identificacionUsuario("1111111111")
                .tipoUsuario(USUARIO_AFILIADO)
                .build();

        mvc.perform(MockMvcRequestBuilders
                .post("/prestamo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prestamoServiceInt.createPrestamo(prestamo))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fechaMaximaDevolucion").exists());

        mvc.perform(MockMvcRequestBuilders
                .post("/prestamo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prestamoServiceInt.createPrestamo(prestamo))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fechaMaximaDevolucion").exists());

    }

    @Test
    public void prestamoConTipoDeUsuarioNoPermitidoDeberiaRetornarExcepcion() throws Exception {

        Prestamo prestamo= Prestamo.builder()
                .isbn("EQWQW8545")
                .identificacionUsuario("1111111111")
                .tipoUsuario(USUARIO_DESCONOCIDO)
                .build();

        mvc.perform(MockMvcRequestBuilders
                .post("/prestamo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(prestamoServiceInt.createPrestamo(prestamo))))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", is("Tipo de usuario no permitido en la biblioteca")));
    }



    public static LocalDate addDaysSkippingWeekends(LocalDate date, int days) {
        LocalDate result = date;
        int addedDays = 0;
        while (addedDays < days) {
            result = result.plusDays(1);
            if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY || result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                ++addedDays;
            }
        }
        return result;
    }
}
