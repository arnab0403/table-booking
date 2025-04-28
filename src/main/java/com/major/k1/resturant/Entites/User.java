package com.major.k1.resturant.Entites;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.util.HashSet;
import java.util.Set;
@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true , nullable = false )
    private String username;
    @Column(unique = true , nullable = false)
    private String email;
    @Column( nullable = false)
    private String password;
    private Long phone;
    private String address;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles= new HashSet<>();
    private String image;
    @OneToMany(mappedBy = "owner")
    private Set<Restaurant> restaurants = new HashSet<>();

}
