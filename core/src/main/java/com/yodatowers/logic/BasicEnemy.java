package com.yodatowers.logic;
import com.badlogic.gdx.graphics.Texture;

public class BasicEnemy extends Enemy {
    public BasicEnemy(Texture texture, float x, float y) {
        super(texture, x, y, 3/5f, 3/4f, 2f, 1); //
    }
}
