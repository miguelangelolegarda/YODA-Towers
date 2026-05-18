package com.yodatowers.factions;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FactionBuffState {
    private final EnumMap<FactionType, FactionStatBlock> statsByFaction;
    private final EnumMap<FactionType, LinkedHashMap<Integer, Boolean>> activeThresholds;

    public FactionBuffState() {
        statsByFaction = new EnumMap<>(FactionType.class);
        activeThresholds = new EnumMap<>(FactionType.class);
        for (FactionType factionType : FactionType.values()) {
            statsByFaction.put(factionType, new FactionStatBlock());
            activeThresholds.put(factionType, new LinkedHashMap<>());
        }
    }

    public void reset() {
        for (FactionStatBlock statBlock : statsByFaction.values()) {
            statBlock.reset();
        }
        for (LinkedHashMap<Integer, Boolean> thresholds : activeThresholds.values()) {
            thresholds.clear();
        }
    }

    public FactionStatBlock getStats(FactionType factionType) {
        return statsByFaction.get(factionType);
    }

    public void markThreshold(FactionType factionType, int threshold, boolean active) {
        activeThresholds.get(factionType).put(threshold, active);
    }

    public Map<Integer, Boolean> getThresholds(FactionType factionType) {
        return activeThresholds.get(factionType);
    }
}
