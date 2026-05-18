package com.yodatowers.entities.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.yodatowers.entities.projectiles.Projectile;
import com.yodatowers.entities.projectiles.RocketProjectile;
import com.yodatowers.entities.projectiles.visuals.ProjectileVisualData;

public class RocketLauncher extends Weapon {
    public RocketLauncher(Texture projectileTexture, float attacksPerSecond, int damage, int rocketCount, float spreadDegrees, float splashRadius) {
        super("Rocket Launcher", projectileTexture, attacksPerSecond, damage, 1, 8f, 6f, rocketCount, spreadDegrees, splashRadius);
    }

    @Override
    protected Projectile createProjectile(Vector2 origin, float angle, int finalDamage, int totalPiercing, ProjectileVisualData visualData) {
        return new RocketProjectile(projectileTexture, origin.x, origin.y, angle, finalDamage, range, projectileSpeed, splashRadius);
    }
}
