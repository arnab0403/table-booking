package com.major.k1.resturant.DTO;
import com.major.k1.resturant.DTO.SlotDTO;
import com.major.k1.resturant.Entites.Coordinates;

import java.util.List;

//for fetching restaurant list
public class RestaurantDTO {
    private Long id;
    private String name;
    private String description;
    private String place;
    private String openTime;
    private List<String> photos;
    private List<String> menu;
    private List<String> bestDishes;
    private Coordinates coordinates;
    private List<SlotDTO> slotTimes;

    public RestaurantDTO(Long id, String name, String description, String place, String openTime,
                         List<String> photos, List<String> menu, List<String> bestDishes,
                         Coordinates coordinates, List<SlotDTO> slotTimes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.place = place;
        this.openTime = openTime;
        this.photos = photos;
        this.menu = menu;
        this.bestDishes = bestDishes;
        this.coordinates = coordinates;
        this.slotTimes = slotTimes;
    }
    public RestaurantDTO(Long id, String name, List<String> menu, List<String> bestDishes,
                         Coordinates coordinates, List<SlotDTO> slotTimes) {
        this.id = id;
        this.name = name;
        this.menu = menu;
        this.bestDishes = bestDishes;
        this.coordinates = coordinates;
        this.slotTimes = slotTimes;
    }

    // Getters and setters for all fields
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
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
