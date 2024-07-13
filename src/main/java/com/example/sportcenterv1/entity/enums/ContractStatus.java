package com.example.sportcenterv1.entity.enums;

public enum ContractStatus {

    NEW("Nowy"),
    PENDING("Oczekujący"),
    REJECTED("Odrzucony"),
    CONFIRMED("Potwiedzony"),
    IN_PROGRESS("W trakcie"),
    COMPLETED("Zakończony"),
    EXPIRING("Wygasający");

    private final String displayName;

    ContractStatus(String displayName){
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
