package com.major.k1.resturant.DTO;

import lombok.Data;

@Data
public class DtoRegUser {
    private String name;
    private String username;
    private String email;
    private String password;
    private Long phone;
    private String address;
}
