package com.example.coffee.dto;

import com.example.coffee.model.CoffeeEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CoffeeDTO {
  private String id;
  private String userId;
  private String title;
  private String brand;
  private String beans;
  private boolean favorite;

  public CoffeeDTO(final CoffeeEntity entity) {
    this.id = entity.getId();
    this.userId = entity.getUserId();
    this.title = entity.getTitle();
    this.brand = entity.getBrand();
    this.beans = entity.getBeans();
    this.favorite = entity.isFavorite();
  }

  public static CoffeeEntity toEntity(final CoffeeDTO dto) {
    return CoffeeEntity.builder()
        .id(dto.getId())
        .userId(dto.getUserId())
        .title(dto.getTitle())
        .brand(dto.getBrand())
        .beans(dto.getBeans())
        .favorite(dto.isFavorite())
        .build();
  }
}
