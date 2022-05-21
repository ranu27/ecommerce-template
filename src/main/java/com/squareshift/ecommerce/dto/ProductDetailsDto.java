package com.squareshift.ecommerce.dto;

import lombok.Data;

@Data
public class ProductDetailsDto {
    private  Integer product_id;
    private String description;
    private Integer quantity;
}
