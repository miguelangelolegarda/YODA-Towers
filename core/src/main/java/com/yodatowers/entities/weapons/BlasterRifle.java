package com.yodatowers.entities.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.yodatowers.entities.projectiles.LaserProjectile;
import com.yodatowers.entities.projectiles.Projectile;
import com.yodatowers.entities.projectiles.visuals.ProjectileStyle;
import com.yodatowers.entities.projectiles.visuals.ProjectileVisualData;

public class BlasterRifle extends Weapon {
    public BlasterRifle(Texture projectileTexture, float attacksPerSecond, int damage) {
        super("Blaster Rifle", projectileTexture, attacksPerSecond, damage, 1, 8f, 12f, 1, 0f, 0f);
    }

    public BlasterRifle(Texture projectileTexture, float attacksPerSecond, int damage, ProjectileStyle projectileStyle) {
        super("Blaster Rifle", projectileTexture, attacksPerSecond, damage, 1, 8f, 12f, 1, 0f, 0f, projectileStyle);
    }

    @Override
    protected Projectile createProjectile(Vector2 origin, float angle, int finalDamage, int totalPiercing, ProjectileVisualData visualData) {
        return new LaserProjectile(visualData, origin.x, origin.y, angle, finalDamage, totalPiercing, range, projectileSpeed);
    }
}
