package com.yodatowers.factions;

public class FactionBuffCalculator {
    // TODO: Load faction thresholds from run data once relics, enemy factions, and temporary buffs are introduced.
    private static final FactionThreshold[] REBEL_THRESHOLDS = {
        new FactionThreshold(FactionType.REBEL, 2, "Rebel damage"),
        new FactionThreshold(FactionType.REBEL, 3, "Rebel attack speed per Rebel"),
        new FactionThreshold(FactionType.REBEL, 4, "Rebel buffs apply to all factions"),
        new FactionThreshold(FactionType.REBEL, 5, "Rebel buffs become stronger")
    };

    private static final FactionThreshold[] REPUBLIC_THRESHOLDS = {
        new FactionThreshold(FactionType.REPUBLIC, 2, "Republic attack speed"),
        new FactionThreshold(FactionType.REPUBLIC, 3, "Light Side attack speed"),
        new FactionThreshold(FactionType.REPUBLIC, 4, "Light Side piercing and damage"),
        new FactionThreshold(FactionType.REPUBLIC, 5, "Reserved Republic capstone")
    };

    public FactionBuffState calculate(FactionManager factionManager) {
        FactionBuffState state = new FactionBuffState();
        applyRebelBuffs(factionManager, state);
        applyRepublicBuffs(factionManager, state);
        return state;
    }

    private void applyRebelBuffs(FactionManager factionManager, FactionBuffState state) {
        int rebelCount = factionManager.getContribution(FactionType.REBEL);
        for (FactionThreshold threshold : REBEL_THRESHOLDS) {
            state.markThreshold(FactionType.REBEL, threshold.getRequiredCount(), threshold.isActive(rebelCount));
        }

        if (rebelCount < 2) {
            return;
        }

        float strengthMultiplier = rebelCount >= 5 ? 2f : 1f;
        float damageBonus = 0.30f * strengthMultiplier;
        float attackSpeedBonus = rebelCount >= 3 ? rebelCount * 0.10f * strengthMultiplier : 0f;

        if (rebelCount >= 4) {
            for (FactionType factionType : FactionType.values()) {
                state.getStats(factionType).addDamageBonus(damageBonus);
                state.getStats(factionType).addAttackSpeedBonus(attackSpeedBonus);
            }
            return;
        }

        state.getStats(FactionType.REBEL).addDamageBonus(damageBonus);
        state.getStats(FactionType.REBEL).addAttackSpeedBonus(attackSpeedBonus);
    }

    private void applyRepublicBuffs(FactionManager factionManager, FactionBuffState state) {
        int republicCount = factionManager.getContribution(FactionType.REPUBLIC);
        for (FactionThreshold threshold : REPUBLIC_THRESHOLDS) {
            state.markThreshold(FactionType.REPUBLIC, threshold.getRequiredCount(), threshold.isActive(republicCount));
        }

        if (republicCount >= 2) {
            state.getStats(FactionType.REPUBLIC).addAttackSpeedBonus(0.30f);
        }

        if (republicCount >= 3) {
            applyToLightSide(state, statBlock -> statBlock.addAttackSpeedBonus(0.20f));
        }

        if (republicCount >= 4) {
            int lightSideCount = factionManager.getLightSideCount();
            applyToLightSide(state, statBlock -> {
                statBlock.addPiercingBonus(1);
            });
        }

        if (republicCount >= 5) {
            int lightSideCount = factionManager.getLightSideCount();
            applyToLightSide(state, statBlock -> {
                statBlock.addDamageBonus(lightSideCount * 0.10f);
            });
        }
    }

    private void applyToLightSide(FactionBuffState state, FactionBuffApplier applier) {
        for (FactionType factionType : FactionType.values()) {
            if (FactionAlignment.LIGHT_SIDE.contains(factionType)) {
                applier.apply(state.getStats(factionType));
            }
        }
    }

    private interface FactionBuffApplier {
        void apply(FactionStatBlock statBlock);
    }
}
