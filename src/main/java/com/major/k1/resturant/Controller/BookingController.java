package com.major.k1.resturant.Controller;

import com.major.k1.resturant.DTO.BookingRequestDTO;

import com.major.k1.resturant.RazorPayDTO.BookingRequestDto;
import com.major.k1.resturant.RazorPayDTO.ConfirmBookingDto;
import com.major.k1.resturant.RazorPayDTO.RazorpayOrderResponse;
import com.major.k1.resturant.Service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class BookingController {

    @Autowired
    private final BookingService bookingService;

    // STEP 1: Create Razorpay Order
    @PostMapping("/book")
    public ResponseEntity<?> createOrder(@RequestBody BookingRequestDto requestDto, Authentication authentication) {
        try {
            RazorpayOrderResponse response = bookingService.createOrder(requestDto, authentication);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // STEP 2: Confirm booking after Razorpay success
    @PostMapping("/confirm-booking")
    public ResponseEntity<?> confirmBooking(@RequestBody ConfirmBookingDto confirmDto, Authentication authentication) {
        try {
            bookingService.confirmBooking(confirmDto, authentication);
            return ResponseEntity.ok("Booking confirmed and saved.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}