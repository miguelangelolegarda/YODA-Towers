package com.yodatowers.entities.towers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yodatowers.entities.enemies.Enemy;
import com.yodatowers.entities.projectiles.Projectile;
import com.yodatowers.entities.subtowers.SubTower;

import java.util.concurrent.CopyOnWriteArrayList;

public class YodaTower extends Tower {

    private final Sprite sprite;
    private final Texture saberTexture;
    private final CopyOnWriteArrayList<SubTower> subTowers;
    private final Vector2 aimPoint;
    private final int maxSlots;
    private int spread;

    private boolean autoAimEnabled = false;

    public YodaTower(Texture yodaTexture, Texture saberTexture, Viewport viewport) {
        super(0.1f, 6f);

        this.saberTexture = saberTexture;
        this.maxSlots = 5;
        this.sprite = new Sprite(yodaTexture);
        this.sprite.setSize(1 / 2f, 3 / 4f);
        this.spread = 1;

        this.sprite.setPosition(
            (viewport.getWorldWidth() / 2f) - 1 / 4f,
            (viewport.getWorldHeight() / 2f) - 3 / 8f
        );

        this.sprite.setOriginCenter();

        this.subTowers = new CopyOnWriteArrayList<>();

        this.aimPoint = new Vector2(getCenter());
    }

    @Override
    public void update(
        float delta,
        CopyOnWriteArrayList<Enemy> enemies,
        CopyOnWriteArrayList<Projectile> projectiles
    ) {
        super.update(delta, enemies, projectiles);

        for (SubTower subTower : subTowers) {
            subTower.update(delta, enemies, projectiles);
        }
    }

    @Override
    protected Vector2 getAttackOrigin() {
        return getCenter();
    }

    @Override
    protected void fireAt(
        Enemy target,
        CopyOnWriteArrayList<Projectile> projectiles
    ) {

        Vector2 origin = getCenter();

        float angle = getAttackAngle(target);

        projectiles.add(
            new Projectile(
                saberTexture,
                origin.x,
                origin.y,
                1 / 13f,
                1 / 2f,
                10f,
                angle,
                1,
                range
            )
        );
        // Add spreading fire. Every one spread adds a projectile to the right and left of the aim
        if(spread > 1){
            for (int i = 1; i < spread; i++) {
                projectiles.add(
                    new Projectile(
                        saberTexture,
                        origin.x,
                        origin.y,
                        1 / 13f,
                        1 / 2f,
                        10f,
                        angle+(i * 10),
                        1,
                        range
                    )
                );
                projectiles.add(
                    new Projectile(
                        saberTexture,
                        origin.x,
                        origin.y,
                        1 / 13f,
                        1 / 2f,
                        10f,
                        angle-(i * 10),
                        1,
                        range
                    )
                );
            }
        }
    }

    public boolean addSubTower(SubTower subTower) {
        if(subTowers.size() < maxSlots){
            subTowers.add(subTower);
            return true;
        }
        return false;
    }

    public void removeSubTower(SubTower subTower) {
        subTowers.remove(subTower);
    }

    public void updateAim(Vector2 mousePosition) {

        aimPoint.set(mousePosition);

        if (!autoAimEnabled) {

            Vector2 origin = getCenter();

            float angle = MathUtils.atan2(
                mousePosition.y - origin.y,
                mousePosition.x - origin.x
            ) * MathUtils.radiansToDegrees;

            sprite.setRotation(angle);
        }
    }

    public float getAttackAngle(Enemy target) {
        Vector2 origin = getCenter();
        Vector2 targetPoint = autoAimEnabled ? enemyCenter(target) : aimPoint;
        return MathUtils.atan2(targetPoint.y - origin.y, targetPoint.x - origin.x) * MathUtils.radiansToDegrees;
    }

    public void addSpread(int spread){
        this.spread += spread;
    }

    public boolean isAutoAimEnabled() {
        return autoAimEnabled;
    }

    public void setAutoAimEnabled(boolean enabled) {
        this.autoAimEnabled = enabled;
    }

    public boolean isFollowMouseEnabled() {
        return !autoAimEnabled;
    }

    public void setFollowMouseEnabled(boolean enabled) {
        this.autoAimEnabled = !enabled;
    }

    public Vector2 getAimPoint() {
        return new Vector2(aimPoint);
    }

    public Vector2 getCenter() {
        return new Vector2(
            sprite.getX() + sprite.getWidth() / 2f,
            sprite.getY() + sprite.getHeight() / 2f
        );
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public void clampToViewport(Viewport viewport) {

        float maxX = viewport.getWorldWidth() - sprite.getWidth();
        float maxY = viewport.getWorldHeight() - sprite.getHeight();

        sprite.setPosition(
            MathUtils.clamp(sprite.getX(), 0, maxX),
            MathUtils.clamp(sprite.getY(), 0, maxY)
        );
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
