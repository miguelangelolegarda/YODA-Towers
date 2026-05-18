package com.yodatowers.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class LaserSprite {

    private float x;
    private float y;

    private final float angle;
    private final float speed;
    private final float damage;
    private final float maxRange;

    private float traveledDistance;

    private final float beamLength;
    private final float beamWidth;

    private final Color coreColor;
    private final Color glowColor;

    public LaserSprite(
        float x,
        float y,
        float angle,
        float speed,
        float damage,
        float maxRange,
        Color color
    ) {

        this.x = x;
        this.y = y;

        this.angle = angle;
        this.speed = speed;
        this.damage = damage;
        this.maxRange = maxRange;

        this.traveledDistance = 0f;

        this.beamLength = 0.45f;
        this.beamWidth = 0.05f;

        this.coreColor = new Color(color);

        this.glowColor = new Color(
            color.r,
            color.g,
            color.b,
            0.22f
        );
    }

    public boolean update(float delta) {

        float dx = MathUtils.cosDeg(angle) * speed * delta;
        float dy = MathUtils.sinDeg(angle) * speed * delta;

        x += dx;
        y += dy;

        traveledDistance += (float)Math.sqrt(dx * dx + dy * dy);

        return traveledDistance >= maxRange;
    }

    public void draw(ShapeRenderer shapeRenderer) {

        float endX = x + MathUtils.cosDeg(angle) * beamLength;
        float endY = y + MathUtils.sinDeg(angle) * beamLength;

        // OUTER GLOW
        shapeRenderer.setColor(glowColor);

        shapeRenderer.rectLine(
            x,
            y,
            endX,
            endY,
            beamWidth * 2.4f
        );

        // INNER CORE
        shapeRenderer.setColor(coreColor);

        shapeRenderer.rectLine(
            x,
            y,
            endX,
            endY,
            beamWidth
        );
    }

    public Rectangle getBounds() {

        return new Rectangle(
            x - beamWidth,
            y - beamWidth,
            beamWidth * 2f,
            beamWidth * 2f
        );
    }

    public float getDamage() {
        return damage;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
