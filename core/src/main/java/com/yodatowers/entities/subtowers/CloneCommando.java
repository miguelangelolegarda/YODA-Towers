package com.yodatowers.entities.subtowers;

import com.badlogic.gdx.graphics.Texture;
import com.yodatowers.entities.projectiles.visuals.ProjectileStyle;
import com.yodatowers.entities.towers.YodaTower;
import com.yodatowers.entities.weapons.HeavyBlasterRifle;
import com.yodatowers.factions.FactionType;

public class CloneCommando extends SubTower {
    public CloneCommando(YodaTower owner, Texture projectileTexture) {
        super("Clone Commando", owner, 3, FactionType.REPUBLIC, projectileTexture);
    }

    @Override
    protected void configureWeapons(Texture projectileTexture) {
        ProjectileStyle commandoBolt = ProjectileStyle.laser()
            .withScale(1.2f)
            .withGlow(0.85f, 2.25f)
            .withMuzzleFlash(true)
            .withTrail(true);
        addWeapon(new HeavyBlasterRifle(projectileTexture, 6f, 20, commandoBolt));
    }

    @Override
    public float getAttackSpeedMultiplierFor(FactionType targetFaction) {
        return targetFaction == FactionType.REPUBLIC ? 1.35f : 1f;
    }
}
