package com.major.k1.resturant.DTO;

import lombok.Data;

@Data
public class UserChangePassDto {
    private String oldpassword;
    private String newpasssword;
}
