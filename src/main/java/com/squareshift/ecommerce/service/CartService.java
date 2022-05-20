package com.squareshift.ecommerce.service;

import com.squareshift.ecommerce.dto.CartItemsDto;
import com.squareshift.ecommerce.dto.CartStatusMessageDto;
import com.squareshift.ecommerce.dto.ItemDto;
import com.squareshift.ecommerce.dto.ProductDto;
import org.springframework.web.bind.annotation.PathVariable;

public interface CartService {
    CartStatusMessageDto addItemToCart(ItemDto item) throws Exception;
    CartItemsDto getCartItems() throws Exception;
    CartStatusMessageDto emptyCart(String action);
    CartStatusMessageDto getTotalAmount( Long postalCode) throws Exception;
}
