package com.squareshift.ecommerce.service;

import com.squareshift.ecommerce.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CartServiceImpl implements CartService {
    final CartItemsDto cart = new CartItemsDto();

    @Autowired
    WareHouseService wareHouseService;
    @Autowired
    ProductService productService;

    public CartServiceImpl(CartItemsDto cart) {
        this.cart.setItems(new ArrayList<>());
    }

    @Override
    public CartStatusMessageDto addItemToCart(ItemDto item) throws Exception {
        CartStatusMessageDto cartStatusMessageDto = new CartStatusMessageDto();
        ProductDetailsDto productDetailsDto =new ProductDetailsDto();
        ProductDto pdto = productService.getProductById(Long.valueOf(item.getProduct_id()));
        productDetailsDto.setProduct_id(item.getProduct_id());
        if(pdto!= null){
            productDetailsDto.setDescription(pdto.getDescription());
            /*List<ProductDetailsDto> list = new ArrayList<>();
            list.add(productDetailsDto);
            cart.setItems(list);*/
            if(cart.getItems().size() == 0){
                List<ProductDetailsDto> list = new ArrayList<>();
                productDetailsDto.setQuantity(item.getQuantity());
                list.add(productDetailsDto);
                cart.setItems(list);
            }
            else if(cart.getItems().size() > 0){
                for(int i=0; i<cart.getItems().size(); i++) {
                    if(cart.getItems().get(i).getProduct_id() == item.getProduct_id()) {
                        Integer quantity = item.getQuantity() + cart.getItems().get(i).getQuantity();
                        productDetailsDto.setQuantity(quantity);
                        cart.getItems().set(i, productDetailsDto);
                    }
                    else {
                        List<ProductDetailsDto> list = cart.getItems();
                        productDetailsDto.setQuantity(item.getQuantity());
                        list.add(productDetailsDto);
                        cart.setItems(list);
                    }
                }
            }
            cartStatusMessageDto.setStatus("success");
            cartStatusMessageDto.setMessage("Item has been added to cart");
            return cartStatusMessageDto;

        }else{
          cartStatusMessageDto.setStatus("error");
          cartStatusMessageDto.setMessage("Invalid product Id");
          return cartStatusMessageDto;
        }
    }

    @Override
    public CartItemsDto getCartItems() throws Exception {
        if(cart!=null) {
            cart.setStatus("success");
            cart.setMessage("Item available in the cart");
            return cart;
        }
        return null;
    }

    @Override
    public CartStatusMessageDto emptyCart(String action) {
        CartStatusMessageDto cartStatusMessageDto = new CartStatusMessageDto();
        if(action.equals("empty_cart")) {
            if (cart.getItems().removeAll(cart.getItems())) {
                cartStatusMessageDto.setStatus("success");
                cartStatusMessageDto.setMessage("All items have been removed from the cart");
                return cartStatusMessageDto;
            }
        }
        return null;
    }

    @Override
    public CartStatusMessageDto getTotalAmount(Long postalCode) throws Exception {
        CartStatusMessageDto status = new CartStatusMessageDto();
        double total =0, totalSum=0, discounted =0;
        Long totalWeight =0l;
        Long shippingCharges =0l;
        double totalCostAfterDiscount =0;
        if(cart.getItems().size() >0 ) {
            if ((postalCode >= 465535) && (postalCode < 465545)) {
                WarehouseResponseDto distance = wareHouseService.getWareHouseDistanceByPostalCode(postalCode);
                Long dist = distance.getDistance_in_kilometers();

                CartItemsDto items = getCartItems();
                if (items != null) {
                    for (ProductDetailsDto product : items.getItems()) {
                        ProductDto prodDesc = productService.getProductById(Long.valueOf(product.getProduct_id()));
                        //calulating total weight = quantity in cart * weight og product in ProductDto
                        totalWeight += product.getQuantity() * prodDesc.getWeight_in_grams();
                        ////calulating discount weight =
                        totalSum = product.getQuantity() * prodDesc.getPrice();
                        discounted = totalSum * (prodDesc.getDiscount_percentage() / 100);
                        totalCostAfterDiscount += totalSum - discounted;
                    }
                }
                shippingCharges = calculateShppingCharge(dist, totalWeight);
                total = totalCostAfterDiscount + shippingCharges;
                status.setStatus("success");
                status.setMessage("Total value of your shipping cart is - $" + total);
                return status;
            } else {
                status.setStatus("error");
                status.setMessage("Invalid postal code, valid ones are 465535 to 465545.");
                return status;
            }
        }
        status.setStatus("error");
        status.setMessage("No Items in cart.");
        return status;
    }

    Long calculateShppingCharge(Long distance, Long totalweight){
        if(distance < 5 && totalweight < 2010)
            return 12l;
        if(distance < 5 && (totalweight >= 2010 && totalweight < 5010))
            return 14l;
        if(distance < 5 && (totalweight >= 5010 && totalweight < 20010))
            return 16l;
        if(distance < 5 && (totalweight >= 20010))
            return 21l;
        if((distance >= 5  && distance <= 20) && totalweight < 2010)
            return 15l;
        if((distance >= 5  && distance <= 20) && (totalweight >= 2010 && totalweight < 5010))
            return 18l;
        if((distance >= 5  && distance <= 20) && (totalweight >= 5010 && totalweight < 20010))
            return 25l;
        if((distance >= 5  && distance <= 20) && (totalweight >= 20010))
            return 35l;
        if((distance > 20  && distance <= 50) && totalweight < 2010)
            return 20l;
        if((distance > 20  && distance <= 50) && (totalweight >= 2010 && totalweight < 5010))
            return 24l;
        if((distance > 20  && distance <= 50) && (totalweight >= 5010 && totalweight < 20010))
            return 30l;
        if((distance > 20  && distance <= 50) && (totalweight >= 20010))
            return 50l;
        if((distance > 50  && distance <= 500) && totalweight < 2010)
            return 50l;
        if((distance > 50  && distance <= 500) && (totalweight >= 2010 && totalweight < 5010))
            return 55l;
        if((distance > 50  && distance <= 500) && (totalweight >= 5010 && totalweight < 20010))
            return 80l;
        if((distance > 50  && distance <= 500) && (totalweight >= 20010))
            return 90l;
        if((distance > 500  && distance <= 800) && totalweight < 2010)
            return 100l;
        if((distance > 500  && distance <= 800) && (totalweight >= 2010 && totalweight < 5010))
            return 110l;
        if((distance > 500  && distance <= 800) && (totalweight >= 5010 && totalweight < 20010))
            return 130l;
        if((distance > 500  && distance <= 800) && (totalweight >= 20010))
            return 150l;
        if(distance > 800 && totalweight < 2010)
            return 220l;
        if(distance > 800 && (totalweight >= 2010 && totalweight < 5010))
            return 250l;
        if(distance > 800 && (totalweight >= 5010 && totalweight < 20010))
            return 270l;
        if(distance > 800 && (totalweight >= 20010))
            return 300l;

        return 0l;
    }
}