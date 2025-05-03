package com.major.k1.resturant.DTO;
import com.major.k1.resturant.DTO.SlotDTO;
import com.major.k1.resturant.Entites.Coordinates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@Data
@NoArgsConstructor
public class RestaurantDTO {
    private Long id;
    private String name;
    private String description;
    private String place;
    private String openTime;
    private List<String> photos;
    private List<MenuDto> menu;
    private List<String> bestDishes;
    private Coordinates coordinates;
    private List<SlotDTO> slotTimes;



}
