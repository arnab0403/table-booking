package com.major.k1.resturant.Repository;

import com.major.k1.resturant.Entites.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    // Optional: Custom method if needed
    // Optional<Restaurant> findByName(String name);
}
