package com.yodatowers.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yodatowers.Main;
import com.yodatowers.effects.ExplosionEffect;
import com.yodatowers.entities.enemies.Enemy;
import com.yodatowers.entities.projectiles.Projectile;
import com.yodatowers.entities.subtowers.BlasterRifle;
import com.yodatowers.entities.subtowers.RocketLauncher;
import com.yodatowers.entities.subtowers.SubTower;
import com.yodatowers.entities.towers.YodaTower;

import java.util.concurrent.CopyOnWriteArrayList;

public class GameScreen implements Screen {
    final Main game;

    Texture backgroundTexture;
    Texture yodaTexture;
    Texture palpTexture;
    Texture saberTexture;
    Sound yodaDeathSound;
    Sound palpDeathSound;
    SpriteBatch spriteBatch;
    ShapeRenderer shapeRenderer;
    BitmapFont font;
    FitViewport viewport;
    YodaTower yodaTower;
    SubTower blasterRifle;
    SubTower rocketLauncher;
    CopyOnWriteArrayList<Enemy> enemies;
    CopyOnWriteArrayList<Projectile> projectiles;
    CopyOnWriteArrayList<ExplosionEffect> explosionEffects;
    CopyOnWriteArrayList<WeaponToggleButton> weaponButtons;
    WaveManager waveManager;
    int yodaHealth;
    volatile boolean isGameOver;
    ShopManager shop;
    AssetManager assetManager;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Asset manager for easier creation of textures, sounds, etc.
        assetManager = new AssetManager();
        assetManager.load("background.jpg", Texture.class);
        assetManager.load("legoYoda.png", Texture.class);
        assetManager.load("palpatine.png", Texture.class);
        assetManager.load("greenSaber.png", Texture.class);
        assetManager.load("lego-yoda-death-sound-effect.mp3", Sound.class);
        assetManager.load("lego-star-wars-palpatine-hurt-sound.mp3", Sound.class);
        assetManager.finishLoading();

        backgroundTexture = assetManager.get("background.jpg", Texture.class);
        yodaTexture = assetManager.get("legoYoda.png", Texture.class);
        palpTexture = assetManager.get("palpatine.png", Texture.class);
        saberTexture = assetManager.get("greenSaber.png", Texture.class);
        yodaDeathSound = assetManager.get("lego-yoda-death-sound-effect.mp3", Sound.class);
        palpDeathSound = assetManager.get("lego-star-wars-palpatine-hurt-sound.mp3", Sound.class);

        yodaHealth = 3;
        isGameOver = false;

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(0.01f);
        viewport = new FitViewport(8, 5);

        enemies = new CopyOnWriteArrayList<>();
        projectiles = new CopyOnWriteArrayList<>();
        explosionEffects = new CopyOnWriteArrayList<>();
        weaponButtons = new CopyOnWriteArrayList<>();

        yodaTower = new YodaTower(yodaTexture, saberTexture, viewport);
        blasterRifle = new BlasterRifle(yodaTower, saberTexture);
        rocketLauncher = new RocketLauncher(yodaTower, saberTexture);
        yodaTower.addSubTower(blasterRifle);
        yodaTower.addSubTower(rocketLauncher);

        weaponButtons.add(new WeaponToggleButton("Blaster Rifle", 0.25f, 0.08f, 2.05f, 0.42f, blasterRifle));
        weaponButtons.add(new WeaponToggleButton("Rocket Launcher", 2.45f, 0.08f, 2.15f, 0.42f, rocketLauncher));

        waveManager = new WaveManager(viewport, palpTexture);
        shop = new ShopManager(yodaTower, assetManager, waveManager);

        new Thread(() -> {
            while (!isGameOver) {
                logic(1 / 60f);
                try {
                    Thread.sleep(16);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    @Override
    public void render(float delta) {
        input();
        draw();
    }

    private void input() {
        if (isGameOver) return;

        if (waveManager.isInShopPhase()) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                shop.rerollShop();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                if (!shop.getOfferings().isEmpty()) shop.buyTower(0, 1);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                if (shop.getOfferings().size() > 1) shop.buyTower(1, 2);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                waveManager.startNextWave();
            }
            return;
        }

        Vector2 mousePos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        handleWeaponButtonInput(mousePos);

        yodaTower.updateAim(mousePos);
        yodaTower.clampToViewport(viewport);
    }

    private void handleWeaponButtonInput(Vector2 mousePos) {
        if (!Gdx.input.justTouched()) {
            return;
        }

        for (WeaponToggleButton weaponButton : weaponButtons) {
            if (weaponButton.tryToggle(mousePos)) {
                return;
            }
        }
    }

    private void logic(float delta) {
        if (isGameOver) return;

        waveManager.update(delta, enemies);
        if (waveManager.isInShopPhase()) {
            return;
        }

        Vector2 yodaCenter = yodaTower.getCenter();
        for (int j = enemies.size() - 1; j >= 0; j--) {
            Enemy enemy = enemies.get(j);
            enemy.update(delta, yodaCenter.x, yodaCenter.y);

            if (yodaTower.getBounds().overlaps(enemy.getBounds())) {
                enemies.remove(j);
                yodaHealth--;
                yodaDeathSound.play();
                System.out.println("Yoda hit! HP remaining: " + yodaHealth);

                if (yodaHealth <= 0) {
                    System.out.println("GAME OVER!");
                    isGameOver = true;
                    // TODO: Replace console game-over with a proper menu/run-summary screen.
                }
            }
        }

        yodaTower.update(delta, enemies, projectiles);
        updateProjectiles(delta);
        updateExplosionEffects(delta);
    }

    private void updateProjectiles(float delta) {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            boolean outOfRange = projectile.update(delta);
            boolean outOfBounds = projectile.getX() < -projectile.getWidth()
                || projectile.getY() < -projectile.getHeight()
                || projectile.getX() > worldWidth + projectile.getWidth()
                || projectile.getY() > worldHeight + projectile.getHeight();

            if (outOfRange || outOfBounds || !projectile.isActive()) {
                projectiles.remove(i);
                continue;
            }

            int defeatedEnemies = projectile.handleEnemyCollisions(enemies, explosionEffects, shop);
            for (int defeated = 0; defeated < defeatedEnemies; defeated++) {
                palpDeathSound.play();
            }

            if (!projectile.isActive()) {
                projectiles.remove(i);
            }
        }
    }

    private void updateExplosionEffects(float delta) {
        for (int i = explosionEffects.size() - 1; i >= 0; i--) {
            if (explosionEffects.get(i).update(delta)) {
                explosionEffects.remove(i);
            }
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        yodaTower.draw(spriteBatch);

        for (Enemy enemy : enemies) {
            enemy.draw(spriteBatch);
        }
        for (Projectile projectile : projectiles) {
            projectile.draw(spriteBatch);
        }
        spriteBatch.end();

        drawShapeEffectsAndUi();
        drawUiLabels();
    }

    private void drawShapeEffectsAndUi() {
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (ExplosionEffect explosionEffect : explosionEffects) {
            explosionEffect.draw(shapeRenderer);
        }
        for (WeaponToggleButton weaponButton : weaponButtons) {
            weaponButton.drawBackground(shapeRenderer);
        }
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawUiLabels() {
        spriteBatch.begin();
        font.setColor(Color.WHITE);
        for (WeaponToggleButton weaponButton : weaponButtons) {
            weaponButton.drawLabel(spriteBatch, font);
        }
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        if (width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        backgroundTexture.dispose();
        yodaTexture.dispose();
        palpTexture.dispose();
        saberTexture.dispose();
        yodaDeathSound.dispose();
        palpDeathSound.dispose();
    }
}
