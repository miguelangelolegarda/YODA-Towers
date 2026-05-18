package com.yodatowers.entities.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.yodatowers.entities.enemies.Enemy;
import com.yodatowers.entities.projectiles.Projectile;
import com.yodatowers.entities.projectiles.visuals.FactionColorResolver;
import com.yodatowers.entities.projectiles.visuals.ProjectileStyle;
import com.yodatowers.entities.projectiles.visuals.ProjectileVisualData;
import com.yodatowers.entities.subtowers.SubTower;
import com.yodatowers.entities.towers.YodaTower;
import com.yodatowers.factions.FactionManager;

import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Weapon {
    protected final String name;
    protected final Texture projectileTexture;
    protected final float attacksPerSecond;
    protected final int damage;
    protected final int piercing;
    protected final float range;
    protected final float projectileSpeed;
    protected final int projectileCount;
    protected final float spreadDegrees;
    protected final float splashRadius;
    protected final ProjectileStyle projectileStyle;
    protected float cooldownTimer;

    public Weapon(
        String name,
        Texture projectileTexture,
        float attacksPerSecond,
        int damage,
        int piercing,
        float range,
        float projectileSpeed,
        int projectileCount,
        float spreadDegrees,
        float splashRadius
    ) {
        this(
            name,
            projectileTexture,
            attacksPerSecond,
            damage,
            piercing,
            range,
            projectileSpeed,
            projectileCount,
            spreadDegrees,
            splashRadius,
            ProjectileStyle.laser()
        );
    }

    public Weapon(
        String name,
        Texture projectileTexture,
        float attacksPerSecond,
        int damage,
        int piercing,
        float range,
        float projectileSpeed,
        int projectileCount,
        float spreadDegrees,
        float splashRadius,
        ProjectileStyle projectileStyle
    ) {
        this.name = name;
        this.projectileTexture = projectileTexture;
        this.attacksPerSecond = attacksPerSecond;
        this.damage = damage;
        this.piercing = piercing;
        this.range = range;
        this.projectileSpeed = projectileSpeed;
        this.projectileCount = projectileCount;
        this.spreadDegrees = spreadDegrees;
        this.splashRadius = splashRadius;
        this.projectileStyle = projectileStyle;
        this.cooldownTimer = 1f / attacksPerSecond;
    }

    public void update(float delta, YodaTower yodaTower, SubTower owner, CopyOnWriteArrayList<Enemy> enemies, CopyOnWriteArrayList<Projectile> projectiles, FactionManager factionManager) {
        Enemy target = acquireTarget(yodaTower, enemies);
        if (target == null) {
            return;
        }

        float attackSpeedMultiplier = factionManager.getAttackSpeedMultiplier(owner.getFactionType());
        float cooldown = 1f / (attacksPerSecond * attackSpeedMultiplier);
        cooldownTimer += delta;
        if (cooldownTimer < cooldown) {
            return;
        }

        fire(yodaTower, owner, target, projectiles, factionManager);
        cooldownTimer = 0f;
    }

    public void readyNow() {
        cooldownTimer = 1f / attacksPerSecond;
    }

    protected Enemy acquireTarget(YodaTower yodaTower, CopyOnWriteArrayList<Enemy> enemies) {
        Vector2 origin = yodaTower.getCenter();
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

    protected void fire(YodaTower yodaTower, SubTower owner, Enemy target, CopyOnWriteArrayList<Projectile> projectiles, FactionManager factionManager) {
        Vector2 origin = yodaTower.getCenter();
        float baseAngle = yodaTower.getAttackAngle(target);
        int totalPiercing = piercing + factionManager.getPiercingBonus(owner.getFactionType());
        int finalDamage = Math.max(1, Math.round(damage * factionManager.getDamageMultiplier(owner.getFactionType())));
        ProjectileVisualData visualData = FactionColorResolver.laser(owner.getFactionType(), projectileTexture, projectileStyle);

        if (projectileCount <= 1) {
            projectiles.add(createProjectile(origin, baseAngle, finalDamage, totalPiercing, visualData));
            return;
        }

        float startOffset = -spreadDegrees * (projectileCount - 1) / 2f;
        for (int i = 0; i < projectileCount; i++) {
            float angle = baseAngle + startOffset + (spreadDegrees * i);
            projectiles.add(createProjectile(origin, angle, finalDamage, totalPiercing, visualData));
        }
    }

    protected abstract Projectile createProjectile(Vector2 origin, float angle, int finalDamage, int totalPiercing, ProjectileVisualData visualData);

    public String getName() {
        return name;
    }
}
