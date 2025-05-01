package com.major.k1.resturant.Entites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
public class CurrentBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String name;
    private Long restaurantId;
    private Long phone;
    private String email;
    private int seats;
    private double amount;

    private Long slotTimeId;
    private String slotTime;
}

