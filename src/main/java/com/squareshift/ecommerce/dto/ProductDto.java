package com.squareshift.ecommerce.dto;


import lombok.Data;

@Data
public class ProductDto {
    private Integer id;
    private String name;
    private String description;
    private String category;
    private String image;
    private Long discount_percentage;
    private Long weight_in_grams;
    private double price;
}
