package com.yodatowers.entities.projectiles.visuals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class ProjectileVisualData {
    private final Texture texture;
    private final Color tint;
    private final ProjectileStyle style;

    public ProjectileVisualData(Texture texture, Color tint, ProjectileStyle style) {
        this.texture = texture;
        this.tint = new Color(tint);
        this.style = style;
    }

    public Texture getTexture() {
        return texture;
    }

    public Color getTint() {
        return new Color(tint);
    }

    public ProjectileStyle getStyle() {
        return style;
    }
}
