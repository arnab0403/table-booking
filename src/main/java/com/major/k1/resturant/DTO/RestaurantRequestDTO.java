package com.major.k1.resturant.DTO;

import com.major.k1.resturant.Entites.Coordinates;
import lombok.Data;

import java.util.List;
@Data
public class RestaurantRequestDTO {
    private String name;
    private List<String> menu;
    private List<String> bestDishes;
    private Coordinates coordinates;
    private List<String> slotTimes; // just time strings

    // Getters and Setters
}