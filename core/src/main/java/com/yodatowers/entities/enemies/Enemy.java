package com.yodatowers.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Polygon; // Used for snug hitbox, so deleted rectangle import
import com.badlogic.gdx.math.Rectangle;

public abstract class Enemy {
    protected Sprite sprite; // Visual
    protected Polygon hitbox; //Hitbox (Collision)
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
        // Bounds should match sprite's dimensions - Changed from rectangle to snug
        // Shave 15% off edges of the image to ignore transparent background pixels (you can edit bur safe estimate)
        float trimW = width * 0.15f;
        float trimH = height * 0.15f;

        this.hitbox = new Polygon(new float[]{
            trimW, trimH,                   // Point 1: Bottom Left
            width - trimW, trimH,           // Point 2: Bottom Right
            width - trimW, height - trimH,  // Point 3: Top Right
            trimW, height - trimH           // Point 4: Top Left
        });

        this.hitbox.setOrigin(width / 2f, height / 2f);
        this.hitbox.setPosition(x, y);
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
        hitbox.setPosition(sprite.getX(), sprite.getY());
    }

    // Change the return type to Polygon
    public Polygon getHitbox() {
        return hitbox;
    }

    public Rectangle getBounds() {
        return hitbox.getBoundingRectangle();
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
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
