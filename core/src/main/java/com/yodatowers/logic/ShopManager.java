package com.yodatowers.logic;

import java.util.ArrayList;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.yodatowers.entities.subtowers.BasicCloneTrooper;
import com.yodatowers.entities.subtowers.CloneCommando;
import com.yodatowers.entities.subtowers.CloneRocketTrooper;
import com.yodatowers.entities.subtowers.RebelScoutTrooper;
import com.yodatowers.entities.subtowers.RebelTrooper;
import com.yodatowers.entities.subtowers.ResistanceOfficer;
import com.yodatowers.entities.subtowers.SubTower;
import com.yodatowers.entities.towers.YodaTower;

public class ShopManager {
    private int playerGold;
    private int rerollCost;
    private ArrayList<String> currentShopOfferings; // Temporary Strings until @JJ finishes SubTower classes
    private ArrayList<SubTower> shopOfferingsValues;
    private YodaTower yodaTower;
    private AssetManager assetManager;
    private WaveManager waveManager;

    public ShopManager(YodaTower yodaTower, AssetManager assetManager, WaveManager waveManager) {
        this.playerGold = 10; // Starting gold; Change as needed
        this.rerollCost = 2;
        this.currentShopOfferings = new ArrayList<>();
        this.shopOfferingsValues = new ArrayList<>();
        this.yodaTower = yodaTower;
        this.assetManager = assetManager;
        this.waveManager = waveManager;
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
        shopOfferingsValues.clear();

        // Randomly pull 5 items to show the player
        for (int i = 0; i < 5; i++) {
            SubTower offering = createRandomSubTower();
            currentShopOfferings.add(offering.getName() + " (" + offering.getCost() + "-Cost)");
            shopOfferingsValues.add(offering);
        }
        System.out.println("Welcome to the Shop! Offerings: " + currentShopOfferings);
    }

    private SubTower createRandomSubTower() {
        int rng = MathUtils.random(0, 5);
        switch (rng) {
            case 0:
                return new RebelTrooper(yodaTower, rebelLaserTexture());
            case 1:
                return new RebelScoutTrooper(yodaTower, rebelLaserTexture());
            case 2:
                return new ResistanceOfficer(yodaTower, rebelLaserTexture());
            case 3:
                return new BasicCloneTrooper(yodaTower, republicLaserTexture());
            case 4:
                return new CloneRocketTrooper(yodaTower, republicLaserTexture());
            default:
                return new CloneCommando(yodaTower, republicLaserTexture());
        }
    }

    private Texture rebelLaserTexture() {
        return assetManager.get("greenLaserBolt.png", Texture.class);
    }

    private Texture republicLaserTexture() {
        return assetManager.get("blueLaserBolt.png", Texture.class);
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
        SubTower selectedTower = shopOfferingsValues.get(shopSlotIndex);
        int actualCost = selectedTower.getCost();
        if (playerGold >= actualCost) {
            playerGold -= actualCost;

            if(yodaTower.addSubTower(selectedTower)){
                String boughtTower = currentShopOfferings.remove(shopSlotIndex);
                shopOfferingsValues.remove(shopSlotIndex);
                System.out.println("Bought " + boughtTower + "! Gold left: " + playerGold);
                // TODO: Pass bought tower to the Triple-Up logic
                checkTripleUpLogic(boughtTower);
                return true;
            }
            else {
                System.out.println("Subtower slots full! Remove a subtower to buy this");
            }
        }
        System.out.println("Not enough gold!");
        return false;
    }

    // Triple-Up system
    private void checkTripleUpLogic(String newTower) {
        // TODO: Look through Yoda's current inventory & if count == 3, delete them and give user a level 2 version
    }

    // Exit Shop and move to next wave
    public void exitShop(){
        waveManager.startNextWave();
    }
    // UI Getters
    public int getPlayerGold() {
        return playerGold;
    }

    public ArrayList<String> getOfferings() {
        return currentShopOfferings;
    }
}
