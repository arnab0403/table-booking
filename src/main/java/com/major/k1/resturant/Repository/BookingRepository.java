package com.major.k1.resturant.Repository;

import com.major.k1.resturant.Entites.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // You can add custom query methods here if needed
    List<Booking> findByRestaurantId(Long restaurantId );
}
