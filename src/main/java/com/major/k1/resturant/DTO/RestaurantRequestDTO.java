package com.major.k1.resturant.DTO;

import com.major.k1.resturant.Entites.Coordinates;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
public class RestaurantRequestDTO {
    private String name;
    private String description;
    private String place;
    private String openTime;
    private List<String> menu;
    private List<String> bestDishes;
    private Coordinates coordinates;
    private List<String> slotTimes;
    private List<MultipartFile> photos; // For file uploads

    // Getters and Setters
}