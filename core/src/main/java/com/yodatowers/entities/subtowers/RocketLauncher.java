package com.yodatowers.entities.subtowers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.yodatowers.entities.enemies.Enemy;
import com.yodatowers.entities.projectiles.Projectile;
import com.yodatowers.entities.projectiles.RocketProjectile;
import com.yodatowers.entities.towers.YodaTower;

import java.util.concurrent.CopyOnWriteArrayList;

public class RocketLauncher extends SubTower {
    private final Texture projectileTexture;

    public RocketLauncher(YodaTower owner, Texture projectileTexture) {
        super("Rocket Launcher", owner, 2f, 8f);
        this.projectileTexture = projectileTexture;
    }

    @Override
    protected void fireAt(Enemy target, CopyOnWriteArrayList<Projectile> projectiles) {
        Vector2 origin = getAttackOrigin();

        float angle = owner.getAttackAngle(target);

        projectiles.add(
            new RocketProjectile(
                projectileTexture,
                origin.x,
                origin.y,
                angle
            )
        );
    }
}
