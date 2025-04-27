package com.major.k1.resturant.Entites;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor // ✅ Required by JPA
@AllArgsConstructor

public class Coordinates {

    private Double latitude;
    private Double longitude;


    // Getters and Setters
}
