package com.major.k1.resturant.RazorPayDTO;

import lombok.Data;

@Data
public class BookingRequestDto {
    private Long restaurantId;
    private Long slotId;
    private int numberOfSeats;
}

