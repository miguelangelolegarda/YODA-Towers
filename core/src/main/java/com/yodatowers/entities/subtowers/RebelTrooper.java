package com.yodatowers.entities.subtowers;

import com.badlogic.gdx.graphics.Texture;
import com.yodatowers.entities.towers.YodaTower;
import com.yodatowers.entities.weapons.BlasterRifle;
import com.yodatowers.factions.FactionType;

public class RebelTrooper extends SubTower {
    public RebelTrooper(YodaTower owner, Texture projectileTexture) {
        super("Rebel Trooper", owner, 1, FactionType.REBEL, projectileTexture);
    }

    @Override
    protected void configureWeapons(Texture projectileTexture) {
        addWeapon(new BlasterRifle(projectileTexture, 3f, 7));
    }
}
