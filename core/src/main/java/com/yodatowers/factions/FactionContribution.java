package com.yodatowers.factions;

public class FactionContribution {
    private final FactionType factionType;
    private int count;

    public FactionContribution(FactionType factionType) {
        this.factionType = factionType;
        this.count = 0;
    }

    public FactionType getFactionType() {
        return factionType;
    }

    public int getCount() {
        return count;
    }

    public void reset() {
        count = 0;
    }

    public void add(int amount) {
        count += amount;
    }
}
