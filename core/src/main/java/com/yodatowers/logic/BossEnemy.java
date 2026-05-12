package com.yodatowers.logic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;

public class BossEnemy extends Enemy {
    public BossEnemy(Texture texture, float x, float y) {
        super(texture, x, y, 6/5f, 6/4f, 0.8f, 10, 5); // Very big and very slow
        this.sprite.setColor(Color.PURPLE);
    }
}
