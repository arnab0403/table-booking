package com.major.k1.resturant.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.major.k1.resturant.DTO.OtpUserStore;
import com.major.k1.resturant.DTO.PendingUser;
import com.major.k1.resturant.Entites.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.major.k1.resturant.DTO.DtoRegUser;

import com.major.k1.resturant.Repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private OtpUserStore otpUserStore;

    public String registerTemp(String userJson, MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(userJson);
        String email = jsonNode.get("email").asText();
        String username = jsonNode.get("username").asText();

        User existingUser = userRepository.findByEmail(email);
        boolean userExists = (existingUser != null);

        if (!userExists) {
            boolean userNameExist = userRepository.findByUsername(username).isPresent();
            if (!userNameExist) {
            DtoRegUser user = objectMapper.readValue(userJson, DtoRegUser.class);
            String otp = String.format("%06d", new Random().nextInt(999999));

            String tempDir = System.getProperty("java.io.tmpdir") + File.separator + "uploads";
            new File(tempDir).mkdirs();
            String imageName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File savedFile = new File(tempDir, imageName);
            file.transferTo(savedFile);

            PendingUser pendingUser = new PendingUser(user, savedFile.getAbsolutePath(), otp, System.currentTimeMillis());
            otpUserStore.save(user.getEmail(), pendingUser);
            sendOtpEmail(user.getEmail(), otp);
            return "Success";
            }
            else {
                return "Username Already Exist";
            }
        } else {
            return "User Email Already Exist";
        }
    }

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;





    public boolean verifyOtp(String email, String otp, PasswordEncoder passwordEncoder, UserRepository userRepository) throws IOException {
        PendingUser pending = otpUserStore.get(email);
        if (pending == null || !pending.getOtp().equals(otp)) {
            return false;
        }

        DtoRegUser dto = pending.getUser();
        File imageFile = new File(pending.getImagePath());
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";
        new File(uploadDir).mkdirs();
        File finalFile = new File(uploadDir, imageFile.getName());
        Files.copy(imageFile.toPath(), finalFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        User user = new User();
        user.setName(dto.getName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setImage(imageFile.getName());
        user.getRoles().add("user");

        userRepository.save(user);
        otpUserStore.remove(email);
        return true;
    }

    private void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Registration OTP");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
    }

    public boolean changepassword(String username ,String oldpassword, String newpassword){
        User user= userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(!passwordEncoder.matches(oldpassword,user.getPassword())){
            return false;
        }
        user.setPassword(passwordEncoder.encode(newpassword));
        userRepository.save(user);
        return true;
    }
}
