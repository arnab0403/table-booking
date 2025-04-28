package com.major.k1.resturant.Controller;

import com.major.k1.resturant.DTO.RestaurantDTO;
import com.major.k1.resturant.DTO.RestaurantDetailsDTO;
import com.major.k1.resturant.DTO.RestaurantRequestDTO;
import com.major.k1.resturant.DTO.TimeSlotDetailsDTO;
import com.major.k1.resturant.Entites.Restaurant;
import com.major.k1.resturant.Repository.RestaurantRepository;
import com.major.k1.resturant.Service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private RestaurantRepository restaurantRepository;

    //Add New Restaurant
    @PostMapping("/addrestaurant")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody RestaurantRequestDTO dto) {
        Restaurant savedRestaurant = restaurantService.createRestaurant(dto);
        return new ResponseEntity<>(savedRestaurant, HttpStatus.CREATED);
    }


    @GetMapping("/resall")
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        List<RestaurantDTO> restaurantDTOs = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurantDTOs);
    }

    // Endpoint to fetch a specific restaurant by ID
    @GetMapping("/restaurant/{id}")
    public RestaurantDetailsDTO getRestaurantDetails(@PathVariable Long id) {
        return restaurantService.getRestaurantDetails(id);
    }




    //Add new menu to a specific restaurant
    @PostMapping("/{restaurantId}/menu")
    public ResponseEntity<Restaurant> addMenuItem(@PathVariable Long restaurantId, @RequestBody String menuItem) {
        Restaurant restaurant = restaurantService.addMenuItem(restaurantId, menuItem);
        return ResponseEntity.ok(restaurant);
    }
     //Delete menu from a specific restaurant

    @DeleteMapping("/{restaurantId}/menu/{menuItem}")
    public ResponseEntity<Restaurant> removeMenuItem(@PathVariable Long restaurantId, @PathVariable String menuItem) {
        Restaurant restaurant = restaurantService.removeMenuItem(restaurantId, menuItem);
        return ResponseEntity.ok(restaurant);
    }

    // Add time slot

    @PostMapping("/{restaurantId}/slot")
    public ResponseEntity<Restaurant> addSlot(@PathVariable Long restaurantId, @RequestBody String slotTime) {
        Restaurant restaurant = restaurantService.addSlot(restaurantId, slotTime);
        return ResponseEntity.ok(restaurant);
    }

 //Delete time slot

    @DeleteMapping("/{restaurantId}/slot/{slotTime}")
    public ResponseEntity<Restaurant> removeSlot(@PathVariable Long restaurantId, @PathVariable String slotTime) {
        Restaurant restaurant = restaurantService.removeSlot(restaurantId, slotTime);
        return ResponseEntity.ok(restaurant);
    }

    // Add best dishes
    @PostMapping("/{restaurantId}/bestDishes")
    public ResponseEntity<Restaurant> addBestDish(@PathVariable Long restaurantId, @RequestBody String dish) {
        Restaurant restaurant = restaurantService.addBestDish(restaurantId, dish);
        return ResponseEntity.ok(restaurant);
    }

    // Delete best dishes

    @DeleteMapping("/{restaurantId}/bestDishes/{dish}")
    public ResponseEntity<Restaurant> removeBestDish(@PathVariable Long restaurantId, @PathVariable String dish) {
        Restaurant restaurant = restaurantService.removeBestDish(restaurantId, dish);
        return ResponseEntity.ok(restaurant);
    }



}








