package com.yodatowers.entities.towers;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.yodatowers.entities.enemies.Enemy;
import com.yodatowers.entities.projectiles.Projectile;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Tower {
    protected float attackCooldown;
    protected float cooldownTimer;
    protected float range;
    protected boolean enabled;

    public Tower(float attackCooldown, float range) {
        this.attackCooldown = attackCooldown;
        this.cooldownTimer = attackCooldown;
        this.range = range;
        this.enabled = true;
    }

    public void update(float delta, CopyOnWriteArrayList<Enemy> enemies, CopyOnWriteArrayList<Projectile> projectiles) {
        if (!enabled) {
            return;
        }

        cooldownTimer += delta;
        if (cooldownTimer < attackCooldown) {
            return;
        }

        Enemy target = acquireTarget(enemies);
        if (target == null) {
            return;
        }

        fireAt(target, projectiles);
        cooldownTimer = 0f;
    }

    protected Enemy acquireTarget(CopyOnWriteArrayList<Enemy> enemies) {
        Vector2 origin = getAttackOrigin();
        Enemy nearestEnemy = null;
        float nearestDistanceSquared = range * range;

        for (Enemy enemy : enemies) {
            Rectangle bounds = enemy.getBounds();
            float centerX = bounds.x + bounds.width / 2f;
            float centerY = bounds.y + bounds.height / 2f;
            float distanceSquared = origin.dst2(centerX, centerY);

            if (distanceSquared <= nearestDistanceSquared) {
                nearestDistanceSquared = distanceSquared;
                nearestEnemy = enemy;
            }
        }

        return nearestEnemy;
    }

    protected float angleFromTo(Vector2 origin, Vector2 target) {
        return (float) Math.toDegrees(Math.atan2(target.y - origin.y, target.x - origin.x));
    }

    protected Vector2 enemyCenter(Enemy enemy) {
        Rectangle bounds = enemy.getBounds();
        return new Vector2(bounds.x + bounds.width / 2f, bounds.y + bounds.height / 2f);
    }

    protected abstract Vector2 getAttackOrigin();

    protected abstract void fireAt(Enemy target, CopyOnWriteArrayList<Projectile> projectiles);

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
