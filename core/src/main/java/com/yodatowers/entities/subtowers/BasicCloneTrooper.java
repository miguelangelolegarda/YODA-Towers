package com.yodatowers.entities.subtowers;

import com.badlogic.gdx.graphics.Texture;
import com.yodatowers.entities.towers.YodaTower;
import com.yodatowers.entities.weapons.BlasterRifle;
import com.yodatowers.factions.FactionType;

public class BasicCloneTrooper extends SubTower {
    public BasicCloneTrooper(YodaTower owner, Texture projectileTexture) {
        super("Basic Clone Trooper", owner, 1, FactionType.REPUBLIC, projectileTexture);
    }

    @Override
    protected void configureWeapons(Texture projectileTexture) {
        addWeapon(new BlasterRifle(projectileTexture, 4f, 5));
    }
}
