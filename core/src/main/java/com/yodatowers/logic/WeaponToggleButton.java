package com.yodatowers.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.yodatowers.entities.subtowers.SubTower;

public class WeaponToggleButton {
    private final String label;
    private final Rectangle bounds;
    private final SubTower weapon;

    public WeaponToggleButton(String label, float x, float y, float width, float height, SubTower weapon) {
        this.label = label;
        this.bounds = new Rectangle(x, y, width, height);
        this.weapon = weapon;
    }

    public boolean tryToggle(Vector2 clickPosition) {
        if (!bounds.contains(clickPosition)) {
            return false;
        }

        // TODO: Replace this placeholder with shop/loadout ownership checks once those systems exist.
        weapon.setEnabled(!weapon.isEnabled());
        return true;
    }

    public void drawBackground(ShapeRenderer shapeRenderer) {
        if (weapon.isEnabled()) {
            shapeRenderer.setColor(new Color(0.1f, 0.55f, 0.28f, 0.9f));
        } else {
            shapeRenderer.setColor(new Color(0.22f, 0.22f, 0.22f, 0.9f));
        }
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rectLine(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y, 0.02f);
        shapeRenderer.rectLine(bounds.x, bounds.y + bounds.height, bounds.x + bounds.width, bounds.y + bounds.height, 0.02f);
        shapeRenderer.rectLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height, 0.02f);
        shapeRenderer.rectLine(bounds.x + bounds.width, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, 0.02f);
    }

    public void drawLabel(SpriteBatch batch, BitmapFont font) {
        String state = weapon.isEnabled() ? "ON" : "OFF";
        font.draw(batch, label + " " + state, bounds.x + 0.12f, bounds.y + 0.28f);
    }
}
