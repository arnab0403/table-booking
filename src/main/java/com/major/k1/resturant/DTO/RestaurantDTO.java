package com.major.k1.resturant.DTO;
import com.major.k1.resturant.DTO.SlotDTO;
import com.major.k1.resturant.Entites.Coordinates;

import java.util.List;

//for fetching restaurant list
public class RestaurantDTO{

    private Long id;
    private String name;
    private List<String> menu;
    private List<String> bestDishes;
    private Coordinates coordinates;  // Coordinates as embedded object
    private List<SlotDTO> slotTimes;  // List of slot times with availability status

    // Constructor
    public RestaurantDTO(Long id, String name, List<String> menu, List<String> bestDishes,
                         Coordinates coordinates, List<SlotDTO> slotTimes) {
        this.id = id;
        this.name = name;
        this.menu = menu;
        this.bestDishes = bestDishes;
        this.coordinates = coordinates;
        this.slotTimes = slotTimes;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMenu() {
        return menu;
    }

    public void setMenu(List<String> menu) {
        this.menu = menu;
    }

    public List<String> getBestDishes() {
        return bestDishes;
    }

    public void setBestDishes(List<String> bestDishes) {
        this.bestDishes = bestDishes;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public List<SlotDTO> getSlotTimes() {
        return slotTimes;
    }

    public void setSlotTimes(List<SlotDTO> slotTimes) {
        this.slotTimes = slotTimes;
    }
}
