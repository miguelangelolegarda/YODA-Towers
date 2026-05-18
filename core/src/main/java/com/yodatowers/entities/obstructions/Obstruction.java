package com.yodatowers.entities.obstructions;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Obstruction {
    protected Sprite sprite; // Visual
    protected Rectangle bounds; //Hitbox (Collision)

    // Obstruction Properties
    public Obstruction(Texture texture, float x, float y, float width, float height) {
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width, height);
        this.sprite.setPosition(x, y);
        // Bounds should match sprite's dimensions
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }
}
