package com.yodatowers.entities.subtowers;

import com.badlogic.gdx.graphics.Texture;
import com.yodatowers.entities.enemies.Enemy;
import com.yodatowers.entities.projectiles.Projectile;
import com.yodatowers.entities.towers.YodaTower;
import com.yodatowers.entities.weapons.Weapon;
import com.yodatowers.factions.FactionManager;
import com.yodatowers.factions.FactionType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class SubTower {
    protected final String name;
    protected final YodaTower owner;
    protected final int cost;
    protected final FactionType factionType;
    protected final ArrayList<Weapon> weapons;
    protected boolean enabled;

    public SubTower(String name, YodaTower owner, int cost, FactionType factionType, Texture projectileTexture) {
        this.name = name;
        this.owner = owner;
        this.cost = cost;
        this.factionType = factionType;
        this.weapons = new ArrayList<>();
        this.enabled = true;
        configureWeapons(projectileTexture);
    }

    protected abstract void configureWeapons(Texture projectileTexture);

    public void update(float delta, CopyOnWriteArrayList<Enemy> enemies, CopyOnWriteArrayList<Projectile> projectiles, FactionManager factionManager) {
        if (!enabled) {
            return;
        }

        for (Weapon weapon : weapons) {
            weapon.update(delta, owner, this, enemies, projectiles, factionManager);
        }
    }

    protected void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public FactionType getFactionType() {
        return factionType;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled && !this.enabled) {
            for (Weapon weapon : weapons) {
                weapon.readyNow();
            }
        }
        this.enabled = enabled;
    }

    public int getPiercingBonusFor(FactionType targetFaction) {
        return 0;
    }

    public float getAttackSpeedMultiplierFor(FactionType targetFaction) {
        return 1f;
    }
}
