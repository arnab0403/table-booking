package com.major.k1.resturant.Service;

import com.major.k1.resturant.DTO.SlotDTO;
import com.major.k1.resturant.Entites.*;
import com.major.k1.resturant.RazorPayDTO.*;

import com.major.k1.resturant.Repository.*;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CurrentBookingRepository currentBookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private SlotTimeRepository slotTimeRepository;

    private RazorpayClient razorpayClient;

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    @PostConstruct
    public void init() throws RazorpayException {
        this.razorpayClient = new RazorpayClient(keyId, keySecret);
    }


    // STEP 1: Generate Razorpay Order and validate
    public RazorpayOrderResponse createOrder(BookingRequestDto requestDto, Authentication authentication) throws RazorpayException {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Please login");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = restaurantRepository.findById(requestDto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        SlotTime slot = slotTimeRepository.findById(requestDto.getSlotId())
                .orElseThrow(() -> new RuntimeException("Time slot not found"));

        if (!slot.isAvailable() || slot.getAvailableSeats() < requestDto.getNumberOfSeats()) {
            throw new RuntimeException("Not enough seats available at this time.");
        }

        int amountInPaise = 200 * requestDto.getNumberOfSeats() * 100;

        // Creating Razorpay order
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", UUID.randomUUID().toString());

        Order order = razorpayClient.orders.create(orderRequest);

        // Storing the booking request in the temporary store
        BookingRequestData bookingData = new BookingRequestData(
                requestDto.getRestaurantId(),
                requestDto.getSlotId(),
                requestDto.getNumberOfSeats(),
                amountInPaise
        );

        // Log to verify the data is stored
        System.out.println("Stored Razorpay Order ID: " + order.get("id"));
        System.out.println("Booking Data: " + bookingData);

        CurrentBookingStore.store(order.get("id"), bookingData);

        // Return Razorpay order information to the frontend
        return new RazorpayOrderResponse(order.get("id"), amountInPaise, user.getName(), user.getEmail(), user.getPhone());
    }

    // STEP 2: Confirm booking after Razorpay payment
    public void confirmBooking(ConfirmBookingDto confirmDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Please login");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        // Logging the Razorpay Order ID received for confirmation
        System.out.println("Received Razorpay Order ID for confirmation: " + confirmDto.getRazorpayOrderId());

        // Retrieve the stored session data from CurrentBookingStore
        BookingRequestData bookingData = CurrentBookingStore.get(confirmDto.getRazorpayOrderId());

        if (bookingData == null) {
            throw new RuntimeException("Invalid booking session.");
        }

        // Log to ensure the data is being retrieved correctly
        System.out.println("Retrieved Booking Data: " + bookingData);

        SlotTime slot = slotTimeRepository.findById(bookingData.getSlotId())
                .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (!slot.isAvailable() || slot.getAvailableSeats() < bookingData.getNumberOfSeats()) {
            throw new RuntimeException("Insufficient seats remaining.");
        }

        int remainingSeats = slot.getAvailableSeats() - bookingData.getNumberOfSeats();
        slot.setAvailableSeats(remainingSeats);
        slot.setAvailable(remainingSeats > 0);
        slotTimeRepository.save(slot);

        // Save Booking (history)
        Booking booking = new Booking();
        booking.setUserId(user.getId());
        booking.setName(user.getName());
        booking.setEmail(user.getEmail());
        booking.setPhone(user.getPhone());
        booking.setRestaurantId(bookingData.getRestaurantId());
        booking.setSeats(bookingData.getNumberOfSeats());
        booking.setAmount(bookingData.getAmount());
        booking.setRazorpayOrderId(confirmDto.getRazorpayOrderId());
        booking.setBookingTime(LocalDateTime.now());
        bookingRepository.save(booking);

        long idd=bookingData.getSlotId();
        SlotTime slotTime=slotTimeRepository.findById(idd).orElseThrow(null);
        SlotDTO slotDTO=new SlotDTO(slotTime.getTime());

        // Save to current bookings
        CurrentBooking currentBooking = new CurrentBooking();
        currentBooking.setUserId(user.getId());
        currentBooking.setName(user.getName());
        currentBooking.setEmail(user.getEmail());
        currentBooking.setPhone(user.getPhone());
        currentBooking.setRestaurantId(bookingData.getRestaurantId());
        currentBooking.setSeats(bookingData.getNumberOfSeats());
        currentBooking.setAmount(bookingData.getAmount());
        currentBooking.setSlotTimeId(bookingData.getSlotId());
        currentBooking.setSlotTime(slotDTO.getTime());
        currentBookingRepository.save(currentBooking);

        // Clean up stored session
        CurrentBookingStore.remove(confirmDto.getRazorpayOrderId());
    }
}
