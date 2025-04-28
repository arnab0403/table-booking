package com.major.k1.resturant.Repository;

import com.major.k1.resturant.Entites.Restaurant;
import com.major.k1.resturant.Entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByOwner(User owner);
    List<Restaurant> findByOwnerId(Long ownerId);
}
