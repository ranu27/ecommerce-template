package com.squareshift.ecommerce;

import com.squareshift.ecommerce.dto.CartItemsDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeedingConfiguration {

    @Bean
    public CartItemsDto cartItem(){
           return new CartItemsDto();
    }
}
