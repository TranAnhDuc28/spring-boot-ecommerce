package com.demo.ecommerce.controller;

import com.demo.ecommerce.dto.PurchaseRequest;
import com.demo.ecommerce.dto.PurchaseResponse;
import com.demo.ecommerce.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/purchase")
    public PurchaseResponse placeOrder(@RequestBody PurchaseRequest request) {
        return checkoutService.placeOrder(request);
    }
}
