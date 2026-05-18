package com.yodatowers.entities.projectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.yodatowers.effects.ExplosionEffect;
import com.yodatowers.entities.enemies.Enemy;
import com.yodatowers.logic.ShopManager;

import java.util.concurrent.CopyOnWriteArrayList;

public class RocketProjectile extends Projectile {
    private final float explosionRadius;

    public RocketProjectile(Texture texture, float x, float y, float angleDegrees) {
        this(texture, x, y, angleDegrees, 2, 8f, 5f, 0.7f);
    }

    public RocketProjectile(Texture texture, float x, float y, float angleDegrees, int damage, float maxRange, float speed, float explosionRadius) {
        super(texture, x, y, 1 / 8f, 1 / 2f, speed, angleDegrees, damage, maxRange);
        this.explosionRadius = explosionRadius;
        setColor(Color.ORANGE);
    }

    @Override
    protected int onEnemyHit(Enemy enemy, CopyOnWriteArrayList<Enemy> enemies, CopyOnWriteArrayList<ExplosionEffect> effects, ShopManager shop) {
        Vector2 explosionCenter = getCenter();
        for (Enemy nearbyEnemy : enemies) {
            Rectangle bounds = nearbyEnemy.getBounds();
            float enemyCenterX = bounds.x + bounds.width / 2f;
            float enemyCenterY = bounds.y + bounds.height / 2f;

            if (explosionCenter.dst2(enemyCenterX, enemyCenterY) <= explosionRadius * explosionRadius) {
                nearbyEnemy.takeDamage(damage);
            }
        }

        active = false;
        effects.add(new ExplosionEffect(explosionCenter.x, explosionCenter.y, explosionRadius));
        return removeDeadEnemies(enemies, shop);
    }
}
