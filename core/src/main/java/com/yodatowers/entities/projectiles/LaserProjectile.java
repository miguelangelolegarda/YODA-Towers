package com.yodatowers.entities.projectiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class LaserProjectile extends Projectile {
    public LaserProjectile(Texture texture, float x, float y, float angleDegrees) {
        super(texture, x, y, 1 / 64f, 2 / 20f, 4f, angleDegrees, 1, 8f);
        setColor(Color.CYAN);
    }
}
