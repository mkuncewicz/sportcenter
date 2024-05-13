package com.example.sportcenterv1.entity.enums;

public enum ContractType {
    EMPLOYMENT_CONTRACT("Umowa o prace"),
    MANDATE_CONTRACT("Umowa o zlecenie"),
    SPECIFIC_TASK_CONTRACT("Umowa o dzieło"),
    INTERNSHIP("Staż/Praktyki"),
    B2B_CONTRACT("Umowa B2B");

    private final String displayName;

    ContractType(String displayName) {
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
