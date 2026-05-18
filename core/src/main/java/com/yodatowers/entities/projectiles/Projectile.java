package com.yodatowers.entities.projectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.yodatowers.effects.ExplosionEffect;
import com.yodatowers.entities.enemies.Enemy;
import com.yodatowers.entities.projectiles.visuals.ProjectileStyle;
import com.yodatowers.entities.projectiles.visuals.ProjectileVisualData;
import com.yodatowers.logic.ShopManager;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class Projectile {
    protected Sprite sprite;
    protected float speed;
    protected float angleRadians;
    protected int damage;
    protected int remainingHits;
    protected final Set<Enemy> hitEnemies;
    protected final ProjectileVisualData visualData;

    protected float maxRange;
    protected float distanceTraveled;
    protected boolean active;

    public Projectile(Texture texture, float x, float y, float width, float height, float speed, float angleDegrees, int damage, float maxRange) {
        this(texture, x, y, width, height, speed, angleDegrees, damage, maxRange, 1);
    }

    public Projectile(Texture texture, float x, float y, float width, float height, float speed, float angleDegrees, int damage, float maxRange, int piercing) {
        this(new ProjectileVisualData(texture, Color.WHITE, ProjectileStyle.laser()), x, y, width, height, speed, angleDegrees, damage, maxRange, piercing);
    }

    public Projectile(ProjectileVisualData visualData, float x, float y, float width, float height, float speed, float angleDegrees, int damage, float maxRange, int piercing) {
        this.visualData = visualData;
        this.sprite = new Sprite(visualData.getTexture());
        float visualScale = visualData.getStyle().getScale();
        this.sprite.setSize(width * visualScale, height * visualScale);
        this.sprite.setOriginCenter();
        this.sprite.setPosition(x - sprite.getWidth() / 2f, y - sprite.getHeight() / 2f);
        this.sprite.setRotation(angleDegrees);
        this.sprite.setColor(visualData.getTint());

        this.speed = speed;
        this.angleRadians = (float) Math.toRadians(angleDegrees);
        this.damage = damage;
        this.remainingHits = Math.max(1, piercing);
        this.maxRange = maxRange;
        this.distanceTraveled = 0f;
        this.active = true;
        this.hitEnemies = new HashSet<>();
    }

    protected void setColor(Color color) {
        sprite.setColor(color);
    }

    public boolean update(float delta) {
        if (!active) {
            return true;
        }

        float xMovement = speed * (float) Math.cos(angleRadians) * delta;
        float yMovement = speed * (float) Math.sin(angleRadians) * delta;

        sprite.translate(xMovement, yMovement);

        // Track how far it has gone using the pyth theorem?? MAAAAA change this if di tama ung formula
        float movementDistance = (float) Math.sqrt(xMovement * xMovement + yMovement * yMovement);
        distanceTraveled += movementDistance;

        // This bool returns true if the projectile has exceeded its range and should be destroyed.
        return distanceTraveled >= maxRange;
    }

    public int handleEnemyCollisions(CopyOnWriteArrayList<Enemy> enemies, CopyOnWriteArrayList<ExplosionEffect> effects, ShopManager shop) {
        int defeated = 0;
        for (Enemy enemy : enemies) {
            if (!hitEnemies.contains(enemy) && getBounds().overlaps(enemy.getBounds())) {
                defeated += onEnemyHit(enemy, enemies, effects, shop);
                if (!active) {
                    return defeated;
                }
            }
        }
        return defeated;
    }

    protected int onEnemyHit(Enemy enemy, CopyOnWriteArrayList<Enemy> enemies, CopyOnWriteArrayList<ExplosionEffect> effects, ShopManager shop) {
        hitEnemies.add(enemy);
        enemy.takeDamage(damage);
        remainingHits--;
        if (remainingHits <= 0) {
            active = false;
        }
        return removeDeadEnemies(enemies, shop);
    }

    protected int removeDeadEnemies(CopyOnWriteArrayList<Enemy> enemies, ShopManager shop) {
        int defeated = 0;
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            if (enemy.isDead()) {
                shop.addGold(enemy.getValue());
                enemies.remove(i);
                defeated++;
            }
        }
        return defeated;
    }

    public boolean isActive() {
        return active;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public int getDamage() {
        return damage;
    }

    public Vector2 getCenter() {
        return new Vector2(sprite.getX() + sprite.getWidth() / 2f, sprite.getY() + sprite.getHeight() / 2f);
    }

    // For screen bounds checking at main
    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }
}
