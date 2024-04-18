package com.example.coffee.controller;

import com.example.coffee.dto.CoffeeDTO;
import com.example.coffee.dto.ResponseDTO;
import com.example.coffee.model.CoffeeEntity;
import com.example.coffee.service.CoffeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("coffee")
public class CoffeeController {

    @Autowired
    private CoffeeService service;

    @PostMapping
    public ResponseEntity<?> createCoffee(@RequestBody CoffeeDTO dto) {
        try {
            CoffeeEntity entity = CoffeeDTO.toEntity(dto);

            List<CoffeeEntity> entities = service.create(entity);
            List<CoffeeDTO> dtos = entities.stream().map(CoffeeDTO::new).collect(Collectors.toList());
            ResponseDTO<CoffeeDTO> response = ResponseDTO.<CoffeeDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<CoffeeDTO> response = ResponseDTO.<CoffeeDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveCoffee(@RequestBody(required = false) CoffeeDTO dto) {
        if (dto != null && dto.getTitle() != null) {
            List<CoffeeEntity> entities = service.retrieveByTitle(dto.getTitle());
            List<CoffeeDTO> dtos = entities.stream().map(CoffeeDTO::new).collect(Collectors.toList());
            ResponseDTO<CoffeeDTO> response = ResponseDTO.<CoffeeDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } else {
            List<CoffeeEntity> entities = service.retrieveAll();
            List<CoffeeDTO> dtos = entities.stream().map(CoffeeDTO::new).collect(Collectors.toList());
            ResponseDTO<CoffeeDTO> response = ResponseDTO.<CoffeeDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCoffee(@RequestBody CoffeeDTO dto) {
        CoffeeEntity entity = CoffeeDTO.toEntity(dto);
        CoffeeEntity updatedEntity = service.update(entity);
        CoffeeDTO updatedDto = new CoffeeDTO(updatedEntity);
        ResponseDTO<CoffeeDTO> response = ResponseDTO.<CoffeeDTO>builder().data(Collections.singletonList(updatedDto))
                .build();

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCoffee(@RequestBody CoffeeDTO dto) {
        try {
            String temporaryUserId = "Sooyeon Hong";

            CoffeeEntity entity = CoffeeDTO.toEntity(dto);
            entity.setUserId(temporaryUserId);

            List<CoffeeEntity> entities = service.delete(entity);
            List<CoffeeDTO> dtos = entities.stream().map(CoffeeDTO::new).collect(Collectors.toList());
            ResponseDTO<CoffeeDTO> response = ResponseDTO.<CoffeeDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<CoffeeDTO> response = ResponseDTO.<CoffeeDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }
    }
}
