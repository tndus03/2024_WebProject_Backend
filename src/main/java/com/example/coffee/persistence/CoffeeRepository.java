package com.example.coffee.persistence;

import com.example.coffee.model.CoffeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoffeeRepository extends JpaRepository<CoffeeEntity, String> {
    List<CoffeeEntity> findByUserId(String userId);
    List<CoffeeEntity> findByTitle(String title);
}
