package com.example.sportcenterv1.entity.enums;

public enum CourtType {

    WOOD("Drewno"),
    CONCRETE("Beton"),
    TAR("Tar"),
    SYNTHETIC("Syntetyczny");

    private final String displayName;

    CourtType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
