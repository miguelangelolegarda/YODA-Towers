package com.yodatowers.logic;

import java.util.ArrayList;
import com.badlogic.gdx.math.MathUtils;
import com.yodatowers.entities.subtowers.*;

public class ShopManager {
    private int playerGold;
    private int rerollCost;
    private ArrayList<String> currentShopOfferings; // Temporary Strings until @JJ finishes SubTower classes

    public ShopManager() {
        this.playerGold = 10; // Starting gold; Change as needed
        this.rerollCost = 2;
        this.currentShopOfferings = new ArrayList<>();
    }

    // Econ
    public void addGold(int amount) {
        playerGold += amount;
    }

    public void calculateWaveInterest() {
        // 1 extra gold for every 15 gold you currently hold (Max 5) [Math.min(playerGold / every, maximum)]
        int interest = Math.min(playerGold / 15, 5);
        playerGold += interest;
        System.out.println("Wave ended! Earned " + interest + " interest. Total Gold: " + playerGold);
    }

    // Display
    public void generateShop() {
        currentShopOfferings.clear();

        // Randomly pull 5 items to show the player
        for (int i = 0; i < 5; i++) {
            // TODO: Replace these strings with actual SubTower classes later
            int rng = MathUtils.random(1, 100);
            if (rng < 50) currentShopOfferings.add("Basic Battle Droid (1-Cost)");
            else if (rng < 80) currentShopOfferings.add("Clone Trooper (1-Cost)");
            else currentShopOfferings.add("Heavy Battle Droid (2-Cost)");
        }
        System.out.println("Welcome to the Shop! Offerings: " + currentShopOfferings);
    }

    // Transactions
    public void rerollShop() {
        if (playerGold >= rerollCost) {
            playerGold -= rerollCost;
            generateShop();
            System.out.println("Rerolled! Gold remaining: " + playerGold);
        } else {
            System.out.println("Not enough gold to reroll!");
        }
    }

    public boolean buyTower(int shopSlotIndex, int towerCost) {
        if (playerGold >= towerCost) {
            playerGold -= towerCost;
            String boughtTower = currentShopOfferings.remove(shopSlotIndex);

            System.out.println("Bought " + boughtTower + "! Gold left: " + playerGold);

            // TODO: Pass bought tower to the Triple-Up logic
            checkTripleUpLogic(boughtTower);

            return true;
        }
        System.out.println("Not enough gold!");
        return false;
    }

    // Triple-Up system
    private void checkTripleUpLogic(String newTower) {
        // TODO: Look through Yoda's current inventory & if count == 3, delete them and give user a level 2 version
    }

    // UI Getters
    public int getPlayerGold() {
        return playerGold;
    }

    public ArrayList<String> getOfferings() {
        return currentShopOfferings;
    }
}
