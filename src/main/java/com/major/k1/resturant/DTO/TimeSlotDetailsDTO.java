package com.major.k1.resturant.DTO;


import lombok.AllArgsConstructor;
//for fetching specific restaurant data
@AllArgsConstructor
public class TimeSlotDetailsDTO {
    private Long id;
    private String time;
    private boolean available;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}