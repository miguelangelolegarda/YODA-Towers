package com.yodatowers.factions;

import com.yodatowers.entities.subtowers.SubTower;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class FactionManager {
    private final EnumMap<FactionType, FactionContribution> contributions;
    private final FactionBuffCalculator buffCalculator;
    private FactionBuffState buffState;
    private List<SubTower> activeSubTowers;

    public FactionManager() {
        this.contributions = new EnumMap<>(FactionType.class);
        for (FactionType factionType : FactionType.values()) {
            contributions.put(factionType, new FactionContribution(factionType));
        }
        this.buffCalculator = new FactionBuffCalculator();
        this.buffState = new FactionBuffState();
    }

    public void rebuild(List<SubTower> subTowers) {
        activeSubTowers = subTowers;
        for (FactionContribution contribution : contributions.values()) {
            contribution.reset();
        }

        contributions.get(FactionType.REPUBLIC).add(1);
        contributions.get(FactionType.JEDI).add(1);

        for (SubTower subTower : subTowers) {
            if (subTower.isEnabled()) {
                contributions.get(subTower.getFactionType()).add(1);
            }
        }

        buffState = buffCalculator.calculate(this);
    }

    public int getContribution(FactionType factionType) {
        return contributions.get(factionType).getCount();
    }

    public Map<FactionType, FactionContribution> getContributions() {
        return contributions;
    }

    public int getLightSideCount() {
        int count = 0;
        for (FactionType factionType : FactionType.values()) {
            if (FactionAlignment.LIGHT_SIDE.contains(factionType)) {
                count += getContribution(factionType);
            }
        }
        return count;
    }

    public FactionBuffState getBuffState() {
        return buffState;
    }

    public float getDamageMultiplier(FactionType factionType) {
        return buffState.getStats(factionType).getDamageMultiplier();
    }

    public int getPiercingBonus(FactionType factionType) {
        int bonus = buffState.getStats(factionType).getPiercingBonus();
        if (activeSubTowers == null) {
            return bonus;
        }

        for (SubTower subTower : activeSubTowers) {
            if (subTower.isEnabled()) {
                bonus += subTower.getPiercingBonusFor(factionType);
            }
        }
        return bonus;
    }

    public float getAttackSpeedMultiplier(FactionType factionType) {
        float multiplier = buffState.getStats(factionType).getAttackSpeedMultiplier();
        if (activeSubTowers == null) {
            return multiplier;
        }

        for (SubTower subTower : activeSubTowers) {
            if (subTower.isEnabled()) {
                multiplier *= subTower.getAttackSpeedMultiplierFor(factionType);
            }
        }
        return multiplier;
    }
}
