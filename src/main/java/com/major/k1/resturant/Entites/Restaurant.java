package com.major.k1.resturant.Entites;


import com.fasterxml.jackson.annotation.JsonManagedReference;
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


    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    //changes

    @Column(columnDefinition = "TEXT")
    private String description;

    private String place;
    private String openTime;

    @ElementCollection
    @CollectionTable(name = "restaurant_photos", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "photo_name") // This will store the file names of the photos
    private List<String> photos = new ArrayList<>();

}
//description , photos , address