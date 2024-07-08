package com.demo.ecommerce.service;

import com.demo.ecommerce.dto.PurchaseRequest;
import com.demo.ecommerce.dto.PurchaseResponse;

public interface CheckoutService {

    PurchaseResponse placeOrder(PurchaseRequest purchase);
}
