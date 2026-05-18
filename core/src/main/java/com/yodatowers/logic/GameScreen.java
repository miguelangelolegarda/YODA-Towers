package com.yodatowers.logic;

import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yodatowers.effects.ExplosionEffect;
import com.yodatowers.entities.enemies.Enemy;
import com.yodatowers.entities.projectiles.Projectile;
import com.yodatowers.entities.subtowers.BlasterRifle;
import com.yodatowers.entities.subtowers.RocketLauncher;
import com.yodatowers.entities.subtowers.SubTower;
import com.yodatowers.entities.towers.YodaTower;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameScreen implements Screen{ //modify extends and implements
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

    //PAUSE LOGIC
    private volatile boolean paused = false; 
    GlyphLayout pausedTitleLayout;
    GlyphLayout pausedHintLayout;
    private ShapeRenderer shape;


    @Override
    public void show() { //show instead of create
        backgroundTexture = new Texture("background.jpg");
        yodaTexture = new Texture("legoYoda.png");
        palpTexture = new Texture("palpatine.png");
        saberTexture = new Texture("greenSaber.png");
        yodaDeathSound = Gdx.audio.newSound(Gdx.files.internal("lego-yoda-death-sound-effect.mp3"));
        palpDeathSound = Gdx.audio.newSound(Gdx.files.internal("lego-star-wars-palpatine-hurt-sound.mp3"));

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

        //PAUSE
        font = new BitmapFont();
        shape = new ShapeRenderer();
        font.getData().setScale(0.035f);
        pausedTitleLayout = new GlyphLayout();
        pausedHintLayout = new GlyphLayout();

        new Thread(() -> {
            while (!isGameOver) {
                if (!paused) { //PAUSE LOGIC
                    logic(1 / 60f); // doesnt render when paused
                }
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) { // When ESC is pressed
            if (!paused) { // Pause when not paused
                pause(); 
            } else { // Resume when paused
                resume(); 
            }
        }

        // Game logic runs normally when not paused
        if (!paused) { 
            input(); 
        }

        draw(); //always allo draw screen
    }

    private void input() {
        if (isGameOver) return;

        Vector2 mousePos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        handleWeaponButtonInput(mousePos);

        yodaTower.updateAim(mousePos);
        yodaTower.clampToViewport(viewport);

        if (waveManager.isInShopPhase() && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            waveManager.startNextWave();
        }
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

            int defeatedEnemies = projectile.handleEnemyCollisions(enemies, explosionEffects);
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

        if (paused){
            shape.setProjectionMatrix(viewport.getCamera().combined); //shapes follow viewport and game camera
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); // enables alpha transparency
            
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(0, 0, 0, 0.7f);
            shape.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight()); //covers whole screen
            shape.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);

            // Draw paused text centered on screen
            spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
            spriteBatch.begin();
            font.setColor(Color.WHITE);
            String title = "GAME PAUSED";
            String hint = "Press ESC to Resume";
            pausedTitleLayout.setText(font, title);
            pausedHintLayout.setText(font, hint);
            float titleX = (viewport.getWorldWidth() - pausedTitleLayout.width) / 2f;
            float titleY = viewport.getWorldHeight() / 2f + pausedTitleLayout.height / 2f;
            float hintX = (viewport.getWorldWidth() - pausedHintLayout.width) / 2f;
            float hintY = viewport.getWorldHeight() / 2f - pausedHintLayout.height;
            font.draw(spriteBatch, pausedTitleLayout, titleX, titleY);
            font.draw(spriteBatch, pausedHintLayout, hintX, hintY);
            spriteBatch.end();

        }
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
        paused = true;
        System.out.println("Game Paused");
    }

    @Override
    public void resume() {
        paused = false;
        System.out.println("Game Resumed");
    }

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
        //PAUSE LOGIC
        if (font != null) font.dispose();
        if (shape != null) shape.dispose();
    }

    @Override
    public void hide(){
        
    }
}
