package com.major.k1.resturant.DTO;

import com.major.k1.resturant.Entites.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
//for fetching specific restaurant details
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDetailsDTO {
    private Long id;
    private String name;
    private String description;
    private String place;
    private String openTime;
    private List<String> photos; // Stores photo file names or URLs
    private List<String> menu;
    private List<String> bestDishes;
    private Coordinates coordinates;
    private List<TimeSlotDetailsDTO> slotTimes;
}
