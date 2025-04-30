package com.major.k1.resturant.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

//for fetching specific restaurant data
@AllArgsConstructor
@Data
public class TimeSlotDetailsDTO {
    private Long id;
    private String time;
    private boolean available;
    private int availableSeats;

    // Getters and Setters

}