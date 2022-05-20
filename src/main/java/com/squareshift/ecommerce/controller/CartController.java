package com.squareshift.ecommerce.controller;

import com.squareshift.ecommerce.dto.*;
import com.squareshift.ecommerce.service.CartService;
import com.squareshift.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {

    @Autowired
    CartService cartService;

    @PostMapping(value = "/v1/cart/item/add")
    public CartStatusMessageDto addItemToCart(@RequestBody ItemDto item) throws Exception{
        return cartService.addItemToCart(item);
    }

    @GetMapping(value = "/v1/cart/item/get")
    public CartItemsDto getCartItems() throws Exception{
        return cartService.getCartItems();
    }

    @PostMapping(value = "/v1/cart/items/empty")
    public CartStatusMessageDto emptyCart(@RequestBody CartEmptyActionDto action) throws Exception{
        return cartService.emptyCart(action.getAction());
    }

    @GetMapping(value = "/v1/cart/checkout-value/{postalCode}")
    public CartStatusMessageDto getTotalAmount(@PathVariable Long postalCode) throws Exception{
        return cartService.getTotalAmount(postalCode);
    }
}
