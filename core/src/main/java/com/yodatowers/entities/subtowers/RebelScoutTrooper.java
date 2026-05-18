package com.yodatowers.entities.subtowers;

import com.badlogic.gdx.graphics.Texture;
import com.yodatowers.entities.towers.YodaTower;
import com.yodatowers.entities.weapons.SniperBlaster;
import com.yodatowers.factions.FactionType;

public class RebelScoutTrooper extends SubTower {
    public RebelScoutTrooper(YodaTower owner, Texture projectileTexture) {
        super("Rebel Scout Trooper", owner, 2, FactionType.REBEL, projectileTexture);
    }

    @Override
    protected void configureWeapons(Texture projectileTexture) {
        addWeapon(new SniperBlaster(projectileTexture, 2f, 20, 3));
    }
}
