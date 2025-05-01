package com.major.k1.resturant.DTO;

import lombok.Data;
//For fetching time slots for restaurant list
@Data
public class SlotDTO {

    private Long id;
    private String time;
    private boolean available;

    // Constructor
    public SlotDTO(Long id, String time, boolean available) {
        this.id = id;
        this.time = time;
        this.available = available;
    }
    public SlotDTO(String time) {
        this.time = time;

    }

    public SlotDTO(String time, boolean available) {
        this.time = time;
        this.available = available;
    }

    // Getters and setters
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
