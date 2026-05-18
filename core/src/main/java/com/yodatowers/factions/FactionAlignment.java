package com.yodatowers.factions;

import java.util.EnumSet;
import java.util.Set;

public enum FactionAlignment {
    LIGHT_SIDE(EnumSet.of(FactionType.REBEL, FactionType.REPUBLIC, FactionType.JEDI));

    private final Set<FactionType> factions;

    FactionAlignment(Set<FactionType> factions) {
        this.factions = factions;
    }

    public boolean contains(FactionType factionType) {
        return factions.contains(factionType);
    }
}
