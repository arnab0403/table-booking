package com.major.k1.resturant.Repository;

import com.major.k1.resturant.Entites.SlotTime;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SlotTimeRepository extends JpaRepository<SlotTime, Long> {
    Optional<SlotTime> findById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE SlotTime s SET s.availableSeats = :totalSeats, s.available = true WHERE s.restaurant.id = :restaurantId")
    void resetSlotsByRestaurantId(@Param("restaurantId") Long restaurantId, @Param("totalSeats") int totalSeats);
    @Modifying
    @Transactional
    @Query("UPDATE SlotTime s SET s.availableSeats = :totalSeats, s.available = true WHERE s.restaurant.id = :restaurantId and s.id =:id")
    void resetSpecificSlot(@Param("restaurantId") Long restaurantId, @Param("totalSeats") int totalSeats,@Param("id") Long id);



}