package com.major.k1.resturant.RazorPayDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RazorpayOrderResponse {
    private String orderId;
    private int amount;
    private String userName;
    private String email;
    private Long phone;
}

