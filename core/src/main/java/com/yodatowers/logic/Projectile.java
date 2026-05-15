package com.yodatowers.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Projectile {
    private Sprite sprite;
    private float speed;
    private float angleRadians;
    private int damage;

    private float maxRange;
    private float distanceTraveled;

    public Projectile(Texture texture, float x, float y, float width, float height, float speed, float angleDegrees, int damage, float maxRange) {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height);
        this.sprite.setOriginCenter();
        this.sprite.setPosition(x, y);
        this.sprite.setRotation(angleDegrees);

        this.speed = speed;
        this.angleRadians = (float) Math.toRadians(angleDegrees);
        this.damage = damage;
        this.maxRange = maxRange;
        this.distanceTraveled = 0f;
    }

    public boolean update(float delta) {

        float xMovement = speed * (float) Math.cos(angleRadians) * delta;
        float yMovement = speed * (float) Math.sin(angleRadians) * delta;

        sprite.translate(xMovement, yMovement);

        // Track how far it has gone using the pyth theorem?? MAAAAA change this if di tama ung formula
        float movementDistance = (float) Math.sqrt(xMovement * xMovement + yMovement * yMovement);
        distanceTraveled += movementDistance;

        // This bool returns true if the projectile has exceeded its range and should be destroyed.
        return distanceTraveled >= maxRange;
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
