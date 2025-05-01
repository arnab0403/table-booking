package com.major.k1.resturant.Repository;

import com.major.k1.resturant.Entites.CurrentBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurrentBookingRepository extends JpaRepository<CurrentBooking, Long> {
    List<CurrentBooking> findByUserId(Long userId); // to fetch current bookings for a user

    void deleteByUserId(Long userId); // optional: to clear current bookings when needed
    List<CurrentBooking> findByRestaurantId(Long restaurantId);
}