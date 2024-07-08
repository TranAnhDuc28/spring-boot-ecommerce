package com.demo.ecommerce.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class PurchaseResponse {

    private String orderTrackingNumber;
}
