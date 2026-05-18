package com.yodatowers.factions;

public class FactionStatBlock {
    private float damageMultiplier;
    private float attackSpeedMultiplier;
    private int piercingBonus;

    public FactionStatBlock() {
        reset();
    }

    public void reset() {
        damageMultiplier = 1f;
        attackSpeedMultiplier = 1f;
        piercingBonus = 0;
    }

    public void addDamageBonus(float additiveBonus) {
        damageMultiplier += additiveBonus;
    }

    public void addAttackSpeedBonus(float additiveBonus) {
        attackSpeedMultiplier += additiveBonus;
    }

    public void addPiercingBonus(int bonus) {
        piercingBonus += bonus;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public float getAttackSpeedMultiplier() {
        return attackSpeedMultiplier;
    }

    public int getPiercingBonus() {
        return piercingBonus;
    }
}
