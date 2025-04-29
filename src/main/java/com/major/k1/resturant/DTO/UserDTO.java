package com.major.k1.resturant.DTO;


import com.major.k1.resturant.Entites.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data

public class UserDTO {
    private String username;
    private String name;
    private String email;
    private long phone;
    private String address;
    private String image;
    private Set<String> roles= new HashSet<>();
    // Constructor
    public UserDTO(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.image = user.getImage();
        this.roles=user.getRoles();
    }

    // Getters and Setters
}
