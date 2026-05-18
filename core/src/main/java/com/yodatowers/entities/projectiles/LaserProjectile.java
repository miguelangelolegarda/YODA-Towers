package com.yodatowers.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yodatowers.entities.projectiles.visuals.ProjectileStyle;
import com.yodatowers.entities.projectiles.visuals.ProjectileVisualData;

public class LaserProjectile extends Projectile {
    public LaserProjectile(Texture texture, float x, float y, float angleDegrees) {
        super(texture, x, y, 1 / 16f, 2 / 5f, 12f, angleDegrees, 1, 8f);
    }

    public LaserProjectile(Texture texture, float x, float y, float angleDegrees, int damage, int piercing, float maxRange, float speed) {
        super(texture, x, y, 1 / 16f, 2 / 5f, speed, angleDegrees, damage, maxRange, piercing);
    }

    public LaserProjectile(ProjectileVisualData visualData, float x, float y, float angleDegrees, int damage, int piercing, float maxRange, float speed) {
        super(visualData, x, y, 1 / 16f, 2 / 5f, speed, angleDegrees, damage, maxRange, piercing);
    }

    @Override
    public void draw(SpriteBatch batch) {
        ProjectileStyle style = visualData.getStyle();
        if (style.getGlowIntensity() > 0f) {
            sprite.setColor(visualData.getTint().r, visualData.getTint().g, visualData.getTint().b, style.getGlowIntensity());
            sprite.setScale(style.getGlowScale());
            sprite.draw(batch);
            sprite.setScale(1f);
        }

        sprite.setColor(visualData.getTint());
        sprite.draw(batch);
    }
}
