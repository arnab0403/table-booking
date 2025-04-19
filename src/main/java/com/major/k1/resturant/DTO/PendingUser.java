package com.major.k1.resturant.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendingUser {
    private DtoRegUser user;
    private String imagePath;
    private String otp;
    private long timestamp;
}
