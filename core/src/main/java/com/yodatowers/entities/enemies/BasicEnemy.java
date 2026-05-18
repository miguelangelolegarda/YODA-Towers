package com.yodatowers.entities.enemies;
import com.badlogic.gdx.graphics.Texture;

public class BasicEnemy extends Enemy {
    public BasicEnemy(Texture texture, float x, float y) {
        super(texture, x, y, 3/20f, 3/16f, 0.5f, 1, 1); //
    }
}
