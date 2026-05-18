package com.yodatowers.factions;

public class FactionThreshold {
    private final FactionType factionType;
    private final int requiredCount;
    private final String description;

    public FactionThreshold(FactionType factionType, int requiredCount, String description) {
        this.factionType = factionType;
        this.requiredCount = requiredCount;
        this.description = description;
    }

    public boolean isActive(int count) {
        return count >= requiredCount;
    }

    public FactionType getFactionType() {
        return factionType;
    }

    public int getRequiredCount() {
        return requiredCount;
    }

    public String getDescription() {
        return description;
    }
}
