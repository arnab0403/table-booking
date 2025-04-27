package com.major.k1.resturant.Entites;


import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@Entity
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    private List<String> menu = new ArrayList<>();

    @ElementCollection
    private List<String> bestDishes = new ArrayList<>();

    @Embedded
    private Coordinates coordinates;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SlotTime> slotTimes = new ArrayList<>();

}
