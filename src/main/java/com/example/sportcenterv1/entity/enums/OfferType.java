package com.example.sportcenterv1.entity.enums;

public enum OfferType {

    ONE_TIME("Jednorazowa usługa"),
    SUBSCRIPTION("Subskrypcja");

    private  final String displayName;

    OfferType(String displayName) {
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
