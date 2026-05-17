package com.yodatowers.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Enemy {
    protected Sprite sprite; // Visual
    protected Rectangle bounds; //Hitbox (Colliusion)
    protected float speed; // Movement units/seconds
    protected int health; // Hit points
    protected int value; // Value for determining how much it adds to a wave

    // Enemy Properties
    public Enemy(Texture texture, float x, float y, float width, float height, float speed, int health, int value) {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height);
        this.sprite.setPosition(x, y);
        this.speed = speed;
        this.health = health;
        this.value = value;
        // Bounds should match sprite's dimensions
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void update(float delta, float targetX, float targetY) {
        // Calculate x and y distance to the target
        float xMovement = targetX - sprite.getX();
        float yMovement = targetY - sprite.getY();
        float len = (float)Math.sqrt(xMovement * xMovement + yMovement * yMovement);

        // Normalize vector so enemy moves at the same speed regardless of distance
        if (len != 0) {
            xMovement /= len;
            yMovement /= len;
        }

        // Move sprite based on direction and time
        sprite.translate(xMovement * speed * delta, yMovement * speed * delta);
        // Hitbox should update as sprites position updates also
        bounds.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public void takeDamage(int amount) {
        health -= amount;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public int getValue() {
        return value;
    }
}
