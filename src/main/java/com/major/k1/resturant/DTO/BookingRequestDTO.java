package com.major.k1.resturant.DTO;

import lombok.Data;

@Data
public class BookingRequestDTO {
    private Long restaurantId;
    private Long slotId;
    private int numberOfSeats;
}
