package com.major.k1.resturant.Controller;

import com.major.k1.resturant.DTO.*;
import com.major.k1.resturant.Entites.CurrentBooking;
import com.major.k1.resturant.Entites.Restaurant;
import com.major.k1.resturant.Entites.SlotTime;
import com.major.k1.resturant.Entites.User;
import com.major.k1.resturant.Repository.CurrentBookingRepository;
import com.major.k1.resturant.Repository.RestaurantRepository;
import com.major.k1.resturant.Repository.SlotTimeRepository;
import com.major.k1.resturant.Repository.UserRepository;
import com.major.k1.resturant.Service.BookingService;
import com.major.k1.resturant.Service.RestaurantService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private CurrentBookingRepository currentBookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SlotTimeRepository slotTimeRepository;
    //Add New Restaurant
    @PostMapping(value = "/addrestaurant",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Restaurant> createRestaurant(
            @RequestPart("restaurant") RestaurantRequestDTO restaurantDTO,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos) {

        System.out.println("Received totalSeats: " + restaurantDTO.getTotalSeats());
        // Set photos in DTO if they exist
        restaurantDTO.setPhotos(photos);

        Restaurant savedRestaurant = restaurantService.createRestaurant(restaurantDTO);
        return new ResponseEntity<>(savedRestaurant, HttpStatus.CREATED);
    }

      // all restaurant
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



    @GetMapping("/restaurants/owned")
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsForOwner(Authentication authentication) {
        // Fetch the username from the Authentication object
        String username = authentication.getName();  // This gives you the logged-in user's username

        // Get the restaurants owned by the logged-in user
        List<RestaurantDTO> restaurants = restaurantService.getRestaurantsForOwner(username);
        return ResponseEntity.ok(restaurants);
    }
    //updated

    //slot time

    @PutMapping("/restaurant/{restaurantId}/slot/{slotId}/availability")
    public ResponseEntity<SlotTime> updateSlotAvailability(@PathVariable Long restaurantId,
                                                           @PathVariable Long slotId,
                                                           @RequestParam boolean available) {
        SlotTime updatedSlot = restaurantService.updateSlotAvailability(restaurantId, slotId, available);
        if (updatedSlot != null) {
            return new ResponseEntity<>(updatedSlot, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    // owner specific restaurant delete
    @DeleteMapping("/delete/res/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id, Authentication authentication) {
        restaurantService.deleteRestaurant(id, authentication);
        return ResponseEntity.ok("Restaurant deleted successfully");
    }

    //reset all slots of the specific restaurant
    @PostMapping("/restaurant/{restaurantId}/reset-slots")
    public ResponseEntity<String> resetSlots(@PathVariable Long restaurantId) {
        restaurantService.resetAvailableSeats(restaurantId);
        return ResponseEntity.ok("All slot seats reset successfully.");
    }

    @PostMapping("specific-slot-reset/{restaurantId}")
    public ResponseEntity<String> resetSpecificSlot(@PathVariable Long restaurantId,@RequestBody Long SlotId){
        restaurantService.resetSpecifTime(restaurantId,SlotId);
        return ResponseEntity.ok("Specific Slot reset sucessfully");
    }


 //List of the current-booking of the specific restaurant
 @GetMapping("/current-booking/{restaurantId}")
 public ResponseEntity<List<CurrentBooking>> getBookingsByRestaurant(@PathVariable Long restaurantId) {
     List<CurrentBooking> bookings = currentBookingRepository.findByRestaurantId(restaurantId);
     return ResponseEntity.ok(bookings);
 }

    @GetMapping("/user/{id}")
    public UserDTO userbyid(@PathVariable Long id){
        User user=userRepository.findById(id) .orElseThrow(() -> new RuntimeException("id not found"));
        UserDTO userDTO = new UserDTO(user);
        return userDTO;
    }

    @GetMapping("/slotBookid/{id}")
    public SlotDTO slotdetails(@PathVariable Long id){
        SlotTime slotTime=  slotTimeRepository.findById(id) .orElseThrow(null);
        SlotDTO slotDTO= new SlotDTO(slotTime.getTime());
        return slotDTO;
    }

}








