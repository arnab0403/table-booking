package com.major.k1.resturant.RazorPayDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingRequestData {
    private Long restaurantId;
    private Long slotId;
    private int numberOfSeats;
    private double amount;
}
