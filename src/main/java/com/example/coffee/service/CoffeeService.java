package com.example.coffee.service;

import com.example.coffee.model.CoffeeEntity;
import com.example.coffee.persistence.CoffeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CoffeeService {

    @Autowired
    private CoffeeRepository repository;

    public List<CoffeeEntity> create(final CoffeeEntity entity) {
        validate(entity);
        repository.save(entity);
        log.info("Entity Id: {} is saved.", entity.getId());

        return repository.findByUserId(entity.getUserId());
    }

     public List<CoffeeEntity> retrieve(final String userId) {
     return repository.findByUserId(userId);
     }

    public List<CoffeeEntity> retrieveByTitle(final String title) {
        return repository.findByTitle(title);
    }

    public List<CoffeeEntity> retrieveAll() {
        return repository.findAll();
    }

    public CoffeeEntity update(final CoffeeEntity entity) { // 수정된 것만 보이는 코드
        validate(entity);

        CoffeeEntity updatedCoffee = null;
        final Optional<CoffeeEntity> original = repository.findById(entity.getId());

        if (original.isPresent()) {
            final CoffeeEntity coffee = original.get();
            coffee.setUserId(entity.getUserId());
            coffee.setTitle(entity.getTitle());
            coffee.setBrand(entity.getBrand());
            coffee.setBeans(entity.getBeans());

            updatedCoffee = repository.save(coffee);
        }

        return updatedCoffee;
    }

    public List<CoffeeEntity> delete(final CoffeeEntity entity) {
        validate(entity);

        try {
            repository.delete(entity);
        } catch (Exception e) {
            log.error("error deleting entity ", entity.getId(), e);
            throw new RuntimeException("error deleting entity " + entity.getId());
        }

        return retrieveAll();
    }

    public void validate(final CoffeeEntity entity) {
        if (entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if (entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }
}
