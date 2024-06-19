package com.example.coffee.controller;

import com.example.coffee.dto.CoffeeDTO;
import com.example.coffee.dto.ResponseDTO;
import com.example.coffee.model.CoffeeEntity;
import com.example.coffee.service.CoffeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("coffee")
public class CoffeeController {

    @Autowired
    private CoffeeService service;

    @PostMapping
    public ResponseEntity<?> createCoffee(@AuthenticationPrincipal String userId, @RequestBody CoffeeDTO dto) {
        try {
            CoffeeEntity entity = CoffeeDTO.toEntity(dto);
            entity.setUserId(userId);
            service.create(entity);
            List<CoffeeEntity> entities = service.retrieveAll(userId);
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
    public ResponseEntity<?> retrieveCoffee(@AuthenticationPrincipal String userId,
            @RequestParam(required = false) String title) {
        List<CoffeeEntity> entities;
        if (title != null && !title.isEmpty()) {
            entities = service.retrieveByTitle(userId, title);
        } else {
            entities = service.retrieve(userId);
        }

        if (entities.isEmpty()) {
            return ResponseEntity.ok().body(ResponseDTO.<CoffeeDTO>builder().error("No items found").build());
        }

        List<CoffeeDTO> dtos = entities.stream().map(CoffeeDTO::new).collect(Collectors.toList());
        ResponseDTO<CoffeeDTO> response = ResponseDTO.<CoffeeDTO>builder().data(dtos).build();

        return ResponseEntity.ok().body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateCoffee(@AuthenticationPrincipal String userId, @RequestBody CoffeeDTO dto) {
        try {
            CoffeeEntity entity = CoffeeDTO.toEntity(dto);
            entity.setUserId(userId);
            CoffeeEntity updatedEntity = service.update(entity);
            CoffeeDTO updatedDto = new CoffeeDTO(updatedEntity);

            return ResponseEntity.ok(updatedDto);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<CoffeeDTO> response = ResponseDTO.<CoffeeDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCoffee(@AuthenticationPrincipal String userId, @RequestBody CoffeeDTO dto) {
        try {
            String title = dto.getTitle();
            if (title == null || title.isEmpty()) {
                throw new IllegalArgumentException("Title must not be null or empty");
            }

            List<CoffeeEntity> entities = service.retrieveByTitle(userId, title);
            if (entities.isEmpty()) {
                throw new IllegalArgumentException("No coffee found with the given title");
            }

            CoffeeEntity entityToDelete = entities.get(0);
            service.delete(entityToDelete);

            List<CoffeeEntity> allEntities = service.retrieveAll(userId);
            List<CoffeeDTO> dtos = allEntities.stream().map(CoffeeDTO::new).collect(Collectors.toList());
            ResponseDTO<CoffeeDTO> response = ResponseDTO.<CoffeeDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<CoffeeDTO> response = ResponseDTO.<CoffeeDTO>builder().error(error).build();

            return ResponseEntity.badRequest().body(response);
        }
    }
}
