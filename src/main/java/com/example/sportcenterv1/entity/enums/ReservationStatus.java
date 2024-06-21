package com.example.sportcenterv1.entity.enums;

public enum ReservationStatus {

    PENDING("Oczekujący"),

    PAID("Opłacone");

    private final String displayName;

    ReservationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
