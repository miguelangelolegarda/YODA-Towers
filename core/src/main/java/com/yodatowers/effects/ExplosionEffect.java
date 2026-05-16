package com.yodatowers.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ExplosionEffect {
    private final float x;
    private final float y;
    private final float radius;
    private final float duration;
    private float timer;

    public ExplosionEffect(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.duration = 0.18f;
        this.timer = 0f;
    }

    public boolean update(float delta) {
        timer += delta;
        return timer >= duration;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        float progress = timer / duration;
        float alpha = 1f - progress;
        shapeRenderer.setColor(new Color(1f, 0.45f, 0.05f, alpha));
        shapeRenderer.circle(x, y, radius * (0.5f + progress * 0.5f), 24);
    }
}
