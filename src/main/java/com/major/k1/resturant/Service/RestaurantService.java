package com.major.k1.resturant.Service;


import com.major.k1.resturant.DTO.*;
import com.major.k1.resturant.Entites.Restaurant;
import com.major.k1.resturant.Entites.SlotTime;
import com.major.k1.resturant.Entites.User;
import com.major.k1.resturant.Repository.RestaurantRepository;
import com.major.k1.resturant.Repository.SlotTimeRepository;
import com.major.k1.resturant.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;
   //Add restaurant
    @Value("${upload.directory}") // Define this in your application.properties
    private String uploadDirectory;

    public Restaurant createRestaurant(RestaurantRequestDTO dto) {
        // Authentication and owner retrieval
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User owner = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create restaurant entity
        Restaurant restaurant = new Restaurant();
        restaurant.setName(dto.getName());
        restaurant.setDescription(dto.getDescription());
        restaurant.setPlace(dto.getPlace());
        restaurant.setOpenTime(dto.getOpenTime());
        restaurant.setMenu(dto.getMenu());
        restaurant.setBestDishes(dto.getBestDishes());
        restaurant.setCoordinates(dto.getCoordinates());
        restaurant.setOwner(owner);
        restaurant.setTotalSeats(dto.getTotalSeats()); // 👈 Set total seats

        // Handle photo uploads
        if (dto.getPhotos() != null && !dto.getPhotos().isEmpty()) {
            List<String> photoNames = handleFileUploads(dto.getPhotos());
            restaurant.setPhotos(photoNames);
        }

        // Handle slot times and set availableSeats for each
        List<SlotTime> slotTimeList = dto.getSlotTimes().stream()
                .map(time -> {
                    SlotTime slot = new SlotTime();
                    slot.setTime(time);
                    slot.setAvailable(true);
                    slot.setAvailableSeats(dto.getTotalSeats()); // 👈 Initialize slot with totalSeats
                    slot.setRestaurant(restaurant);
                    return slot;
                })
                .collect(Collectors.toList());

        restaurant.setSlotTimes(slotTimeList);

        return restaurantRepository.save(restaurant);
    }

    private List<String> handleFileUploads(List<MultipartFile> files) {
        List<String> fileNames = new ArrayList<>();
        Path uploadPath = Paths.get(uploadDirectory);

        try {
            // Create directory if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // Generate unique filename
                    String originalFileName = file.getOriginalFilename();
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                    String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

                    // Save file
                    Path filePath = uploadPath.resolve(uniqueFileName);
                    file.transferTo(filePath.toFile());

                    fileNames.add(uniqueFileName);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store files: " + e.getMessage());
        }

        return fileNames;
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

            // Return RestaurantDTO with all fields
            return new RestaurantDTO(
                    restaurant.getId(),
                    restaurant.getName(),
                    restaurant.getDescription(),
                    restaurant.getPlace(),
                    restaurant.getOpenTime(),
                    restaurant.getPhotos(),
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
                .map(slot -> new TimeSlotDetailsDTO(
                        slot.getId(),
                        slot.getTime(),
                        slot.isAvailable(),
                        slot.getAvailableSeats()
                ))
                .collect(Collectors.toList());

        // Build photo URLs (assuming photos are stored with base URL)
        List<String> photoUrls = restaurant.getPhotos().stream()
                .map(photoName ->  photoName)
                .collect(Collectors.toList());

        return new RestaurantDetailsDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getDescription(),
                restaurant.getPlace(),
                restaurant.getOpenTime(),
                photoUrls, // Now includes full photo URLs
                restaurant.getMenu(),
                restaurant.getBestDishes(),
                restaurant.getCoordinates(),
                slotTimes,
                restaurant.getTotalSeats()
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




    @Autowired
    private SlotTimeRepository slotTimeRepository;

    // updates time slots

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


    // specifi restaurant delete

    @Value("${upload.directory}")
    private String uploadDir;
    public void deleteRestaurant(Long restaurantId, Authentication authentication) {
        String username = authentication.getName();

        // Get the logged-in user
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Get the restaurant
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found"));

        // Verify ownership
        if (!restaurant.getOwner().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not the owner of this restaurant");
        }

        // Delete photo files from the file system
        for (String photoName : restaurant.getPhotos()) {
            Path photoPath = Paths.get(uploadDir, photoName);
            try {
                Files.deleteIfExists(photoPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete photo file: " + photoName, e);
            }
        }

        // Delete restaurant and cascade all linked entities
        restaurantRepository.delete(restaurant);
    }

    //reset all seats of the specific restaurant

    @Transactional
    public void resetAvailableSeats(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        int totalSeats = restaurant.getTotalSeats();
        slotTimeRepository.resetSlotsByRestaurantId(restaurantId, totalSeats);
    }



}
