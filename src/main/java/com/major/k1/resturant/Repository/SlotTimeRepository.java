package com.major.k1.resturant.Repository;

import com.major.k1.resturant.Entites.SlotTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SlotTimeRepository extends JpaRepository<SlotTime, Long> {
    Optional<SlotTime> findById(Long id);
}