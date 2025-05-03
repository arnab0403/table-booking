package com.major.k1.resturant.Entites;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class Menu {
    private String item;
    private double  price;
    @ManyToMany
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

}
