package com.yodatowers.entities.projectiles.visuals;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.yodatowers.factions.FactionType;

public class FactionColorResolver {
    private static final Color REBEL_LASER = new Color(0.25f, 1f, 0.25f, 1f);
    private static final Color REPUBLIC_LASER = new Color(0.35f, 0.7f, 1f, 1f);
    private static final Color JEDI_LASER = new Color(0.45f, 1f, 0.9f, 1f);

    private FactionColorResolver() {
    }

    public static ProjectileVisualData laser(FactionType factionType, Texture texture, ProjectileStyle style) {
        return new ProjectileVisualData(texture, resolveLaserTint(factionType), style);
    }

    public static Color resolveLaserTint(FactionType factionType) {
        switch (factionType) {
            case REBEL:
                return REBEL_LASER;
            case REPUBLIC:
                return REPUBLIC_LASER;
            case JEDI:
                return JEDI_LASER;
            default:
                return Color.WHITE;
        }
    }
}
