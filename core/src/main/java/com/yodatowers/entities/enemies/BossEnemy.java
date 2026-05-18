package com.yodatowers.entities.enemies;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;

public class BossEnemy extends Enemy {
    public BossEnemy(Texture texture, float x, float y) {
        super(texture, x, y, 6/20f, 6/16f, 0.2f, 4, 5); // Very big and very slow
        this.sprite.setColor(Color.PURPLE);
    }
}
