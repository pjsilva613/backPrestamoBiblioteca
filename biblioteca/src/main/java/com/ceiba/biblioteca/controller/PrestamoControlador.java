package com.ceiba.biblioteca.controller;


import com.ceiba.biblioteca.entity.ErrorMessage;
import com.ceiba.biblioteca.entity.Prestamo;
import com.ceiba.biblioteca.service.PrestamoServiceInt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/prestamo")
public class PrestamoControlador {

    @Autowired
    private PrestamoServiceInt prestamoServiceInt;

    @GetMapping
    public ResponseEntity<List<Prestamo>> findAllPrestamos(){
        List<Prestamo> prestamos= prestamoServiceInt.findAllPrestamos();
        if (prestamos == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping(value = "/{id-prestamo}")
    public ResponseEntity<Prestamo> findByIdPrestamo(@PathVariable("id-prestamo") Long id){
        Prestamo prestamo= prestamoServiceInt.findByIdPrestamo(id);
        if (null == prestamo ) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(prestamo);
    }

    @PostMapping
    public ResponseEntity<Prestamo> createPrestamo(@Valid @RequestBody Prestamo prestamo, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(bindingResult));
        }
        Prestamo prestamoDb=prestamoServiceInt.createPrestamo(prestamo);
        return ResponseEntity.ok(prestamoDb);
    }

    private String formatMessage(BindingResult bindingResult) {
        List<Map<String,String>> errors = bindingResult.getFieldErrors().stream()
                .map(err ->{ Map<String,String> error= new HashMap<>();
                 error.put(err.getField(), err.getDefaultMessage());
                return error;}).collect(Collectors.toList());

        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .message(errors)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString= objectMapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}

