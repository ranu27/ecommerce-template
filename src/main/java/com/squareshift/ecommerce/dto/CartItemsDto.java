package com.squareshift.ecommerce.dto;
import lombok.Data;

import java.util.List;

@Data
public class CartItemsDto extends CartStatusMessageDto{
    private List<ProductDetailsDto> items;
}
