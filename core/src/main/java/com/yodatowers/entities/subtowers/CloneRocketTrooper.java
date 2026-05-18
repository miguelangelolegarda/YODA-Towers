package com.yodatowers.entities.subtowers;

import com.badlogic.gdx.graphics.Texture;
import com.yodatowers.entities.towers.YodaTower;
import com.yodatowers.entities.weapons.RocketLauncher;
import com.yodatowers.factions.FactionType;

public class CloneRocketTrooper extends SubTower {
    public CloneRocketTrooper(YodaTower owner, Texture projectileTexture) {
        super("Clone Rocket Trooper", owner, 2, FactionType.REPUBLIC, projectileTexture);
    }

    @Override
    protected void configureWeapons(Texture projectileTexture) {
        addWeapon(new RocketLauncher(projectileTexture, 1f, 5, 3, 12f, 0.7f));
    }
}
