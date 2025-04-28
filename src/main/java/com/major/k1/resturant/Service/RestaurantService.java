package com.major.k1.resturant.Service;


import com.major.k1.resturant.DTO.*;
import com.major.k1.resturant.Entites.Restaurant;
import com.major.k1.resturant.Entites.SlotTime;
import com.major.k1.resturant.Entites.User;
import com.major.k1.resturant.Repository.RestaurantRepository;
import com.major.k1.resturant.Repository.SlotTimeRepository;
import com.major.k1.resturant.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    //Add Restaurant method
    public Restaurant createRestaurant(RestaurantRequestDTO dto) {
        // Get the Authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Get the username (or email) of the currently logged-in user
        String currentUsername = authentication.getName(); // username is typically stored as the principal

        // Fetch the owner from the database based on the logged-in user's username/email
        User owner = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create the restaurant entity
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setMenu(dto.getMenu());
        restaurant.setBestDishes(dto.getBestDishes());
        restaurant.setCoordinates(dto.getCoordinates());
        restaurant.setOwner(owner); // Set the logged-in user as the owner

        // Create SlotTime entities
        List<SlotTime> slotTimeList = dto.getSlotTimes().stream().map(time -> {
            SlotTime slot = new SlotTime();
            slot.setTime(time);
            slot.setAvailable(true); // default to available
            slot.setRestaurant(restaurant); // link back to the restaurant
            return slot;
        }).collect(Collectors.toList());

        restaurant.setSlotTimes(slotTimeList);

        // Save the restaurant in the database
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

    //

    public List<Restaurant> getRestaurantsByOwner(String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return restaurantRepository.findByOwner(owner);
    }
    //owner resturant

    public List<RestaurantDTO> getRestaurantsForOwner(String username) {
        // Fetch the user by username (and handle Optional<User> properly)
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // Fetch the list of restaurants owned by this user
        List<Restaurant> restaurants = restaurantRepository.findByOwnerId(user.getId());

        // Map Restaurant entities to RestaurantDTOs
        return restaurants.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private RestaurantDTO convertToDTO(Restaurant restaurant) {
        // Convert SlotTime entities to SlotDTOs
        List<SlotDTO> slotDTOs = restaurant.getSlotTimes().stream()
                .map(slot -> new SlotDTO(slot.getTime(), slot.isAvailable()))
                .collect(Collectors.toList());

        // Return the RestaurantDTO
        return new RestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getMenu(),
                restaurant.getBestDishes(),
                restaurant.getCoordinates(),
                slotDTOs
        );
    }
    // updated


    //slottime
    @Autowired
    private SlotTimeRepository slotTimeRepository;

    // Other service methods...

    public SlotTime updateSlotAvailability(Long restaurantId, Long slotId, boolean available) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        if (restaurant.isPresent()) {
            Optional<SlotTime> slotTime = slotTimeRepository.findById(slotId);
            if (slotTime.isPresent() && slotTime.get().getRestaurant().getId().equals(restaurantId)) {
                SlotTime slot = slotTime.get();
                slot.setAvailable(available);
                return slotTimeRepository.save(slot);
            }
        }
        return null;  // Return null if the slot or restaurant is not found
    }



}
