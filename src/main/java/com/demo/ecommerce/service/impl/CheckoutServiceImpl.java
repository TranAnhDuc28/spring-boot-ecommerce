package com.demo.ecommerce.service.impl;

import com.demo.ecommerce.dao.CustomerRepository;
import com.demo.ecommerce.dto.PurchaseRequest;
import com.demo.ecommerce.dto.PurchaseResponse;
import com.demo.ecommerce.entity.Customer;
import com.demo.ecommerce.entity.Order;
import com.demo.ecommerce.entity.OrderItem;
import com.demo.ecommerce.service.CheckoutService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public PurchaseResponse placeOrder(PurchaseRequest purchaseRequest) {
        // lấy thông tin đơn hàng từ dto PurchaseRequest (purchase: mua hàng)
        Order order = purchaseRequest.getOrder();

        // generate tracking number
        String orderTrackingNumber = generateOrderTrackingNumber();
        order.setOrderTrackingNumber(orderTrackingNumber);

        // populate order with orderItems
        Set<OrderItem> orderItems = purchaseRequest.getOrderItems();
        orderItems.forEach(item -> order.add(item));

        // populate order with shippingAddress and billingAddress
        order.setShippingAddress(purchaseRequest.getShippingAddress());
        order.setBillingAddress(purchaseRequest.getBillingAddress());

        // populate customer with order
        Customer customer = purchaseRequest.getCustomer();
        customer.add(order);

        // save to the database
        customerRepository.save(customer);

        // return a response
        return PurchaseResponse.builder()
                .orderTrackingNumber(orderTrackingNumber)
                .build();
    }

    private String generateOrderTrackingNumber() {
        // generate a random UUID number
        return UUID.randomUUID().toString();
    }
}
