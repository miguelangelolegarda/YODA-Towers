package com.yodatowers.entities.subtowers;

import com.badlogic.gdx.graphics.Texture;
import com.yodatowers.entities.projectiles.visuals.ProjectileStyle;
import com.yodatowers.entities.towers.YodaTower;
import com.yodatowers.entities.weapons.HeavyBlasterPistol;
import com.yodatowers.factions.FactionType;

public class ResistanceOfficer extends SubTower {
    public ResistanceOfficer(YodaTower owner, Texture projectileTexture) {
        super("Resistance Officer", owner, 3, FactionType.REBEL, projectileTexture);
    }

    @Override
    protected void configureWeapons(Texture projectileTexture) {
        ProjectileStyle officerLaser = ProjectileStyle.laser()
            .withScale(1.25f)
            .withGlow(0.75f, 2.15f)
            .withTrail(true)
            .withImpactEffect(true);
        addWeapon(new HeavyBlasterPistol(projectileTexture, 2f, 25, 3, officerLaser));
    }

    @Override
    public int getPiercingBonusFor(FactionType targetFaction) {
        return targetFaction == FactionType.REBEL ? 1 : 0;
    }
}
