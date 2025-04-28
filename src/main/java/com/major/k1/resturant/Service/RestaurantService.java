package com.major.k1.resturant.Service;


import com.major.k1.resturant.DTO.*;
import com.major.k1.resturant.Entites.Restaurant;
import com.major.k1.resturant.Entites.SlotTime;
import com.major.k1.resturant.Repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    //Add Restaurant method
    public Restaurant createRestaurant(RestaurantRequestDTO dto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setMenu(dto.getMenu());
        restaurant.setBestDishes(dto.getBestDishes());
        restaurant.setCoordinates(dto.getCoordinates());

        // Create SlotTime entities
        List<SlotTime> slotTimeList = dto.getSlotTimes().stream().map(time -> {
            SlotTime slot = new SlotTime();
            slot.setTime(time);
            slot.setAvailable(true); // default to available
            slot.setRestaurant(restaurant); // link back
            return slot;
        }).collect(Collectors.toList());

        restaurant.setSlotTimes(slotTimeList);

        return restaurantRepository.save(restaurant);
    }

         // Menu  add method
    public Restaurant addMenuItem(Long restaurantId, String menuItem) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.getMenu().add(menuItem);
        return restaurantRepository.save(restaurant);
    }

     // Remove Menu item
    public Restaurant removeMenuItem(Long restaurantId, String menuItem) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.getMenu().remove(menuItem);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant addSlot(Long restaurantId, String slotTime) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        SlotTime slot = new SlotTime();
        slot.setTime(slotTime);
        slot.setAvailable(true);
        slot.setRestaurant(restaurant);
        restaurant.getSlotTimes().add(slot);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant removeSlot(Long restaurantId, String slotTime) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.getSlotTimes().removeIf(slot -> slot.getTime().equals(slotTime));
        return restaurantRepository.save(restaurant);
    }

    public Restaurant addBestDish(Long restaurantId, String dish) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.getBestDishes().add(dish);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant removeBestDish(Long restaurantId, String dish) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        restaurant.getBestDishes().remove(dish);
        return restaurantRepository.save(restaurant);
    }


    public List<RestaurantDTO> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream().map(restaurant -> {
            // Convert SlotTimes to SlotDTOs
            List<SlotDTO> slotTimes = restaurant.getSlotTimes().stream()
                    .map(slotTime -> new SlotDTO(slotTime.getId(), slotTime.getTime(), slotTime.isAvailable()))
                    .collect(Collectors.toList());

            // Return RestaurantDTO
            return new RestaurantDTO(
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getMenu(),
                    restaurant.getBestDishes(),
                    restaurant.getCoordinates(),
                    slotTimes
            );
        }).collect(Collectors.toList());
    }



    public RestaurantDetailsDTO getRestaurantDetails(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        List<TimeSlotDetailsDTO> slotTimes = restaurant.getSlotTimes().stream()
                .map(slot -> new TimeSlotDetailsDTO(slot.getId(), slot.getTime(), slot.isAvailable()))
                .collect(Collectors.toList());

        return new RestaurantDetailsDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getMenu(),
                restaurant.getBestDishes(),
                restaurant.getCoordinates(),
                slotTimes
        );
    }


}
