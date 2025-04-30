package com.major.k1.resturant.RazorPayDTO;

import java.util.concurrent.ConcurrentHashMap;

public class CurrentBookingStore {
    private static final ConcurrentHashMap<String, BookingRequestData> bookingMap = new ConcurrentHashMap<>();

    public static void store(String razorpayOrderId, BookingRequestData data) {
        bookingMap.put(razorpayOrderId, data);
    }

    public static BookingRequestData get(String razorpayOrderId) {
        return bookingMap.get(razorpayOrderId);
    }

    public static void remove(String razorpayOrderId) {
        bookingMap.remove(razorpayOrderId);
    }
}
