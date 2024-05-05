package com.example.sportcenterv1.entity.enums;

public enum TurfType {

    NATURAL("Naturalna"),
    ARTIFICIAL("Sztuczna"),
    HYBRID("Hybryda");

    private final String displayName;

    TurfType(String displayName) {
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
