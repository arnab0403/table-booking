package com.major.k1.resturant.RazorPayDTO;

import lombok.Data;

@Data
public class ConfirmBookingDto {
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
}

