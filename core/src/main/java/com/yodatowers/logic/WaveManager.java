package com.yodatowers.logic;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yodatowers.entities.enemies.BasicEnemy;
import com.yodatowers.entities.enemies.BossEnemy;
import com.yodatowers.entities.enemies.Enemy;

import java.util.concurrent.CopyOnWriteArrayList;

public class WaveManager {
//    private float waveTimer;
    private float spawnTimer;
//    private float waveDuration = 60f; // Waves last exactly 1 minute
    private int currentWave;
    private boolean inShopPhase;
    private int waveDifficulty;


    private Viewport viewport;
    private Texture enemyTexture;
    private boolean waveFull;
    private int currentValue;

    public WaveManager(Viewport viewport, Texture enemyTexture) {
        this.viewport = viewport;
        this.enemyTexture = enemyTexture;
        this.currentWave = 1;
//        this.waveTimer = 0f;
        this.spawnTimer = 0f;
        this.inShopPhase = false;
        this.waveFull = false;
        this.waveDifficulty = 5000;
        this.currentValue = 0;
    }

    public void update(float delta, CopyOnWriteArrayList<Enemy> enemies) {
        // If we are in the shop phase, freeze timer and stop spawning enemies
        if (inShopPhase) return;

//        waveTimer += delta;
        spawnTimer += delta;

        //The closer the timer gets to 60 seconds, the faster enemies spawn
        // Capped @ enemies per 0.3 seconds
//        float spawnCooldown = MathUtils.clamp(1.5f - (waveTimer / 60f), 0.3f, 1.5f);
        float spawnCooldown = 0.0000001f;

        if (spawnTimer >= spawnCooldown && !waveFull) {
            spawnTimer = 0;
            spawnEnemy(enemies);
        }

//        if (waveTimer >= waveDuration) {
//            endWave();

        if (waveFull && enemies.isEmpty()) {
            endWave();
        }

    }

    private void spawnEnemy(CopyOnWriteArrayList<Enemy> enemies) {
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float offScreenBuffer = 2f;
        float x = 0;
        float y = 0;

        // Pick a random edge of the screen to spawn the enemy
        int screenSide = MathUtils.random(0, 3);
        switch(screenSide) {
            case 0: // Top
                x = MathUtils.random(0, worldWidth);
                y = worldHeight + offScreenBuffer;
                break;
            case 1: // Left
                x = -offScreenBuffer;
                y = MathUtils.random(0, worldHeight);
                break;
            case 2: // Bottom
                x = MathUtils.random(0, worldWidth);
                y = -offScreenBuffer;
                break;
            case 3: // Right
                x = worldWidth + offScreenBuffer;
                y = MathUtils.random(0, worldHeight);
                break;
        }

        // Randomly select 1 of the 5 enemies, try implementing less chance for boss to spawn later
        int enemyType = MathUtils.random(0, 4);
        Enemy newEnemy;
        switch(enemyType) {
            case 0: newEnemy = new BasicEnemy(enemyTexture, x, y); break;
            case 1: newEnemy = new BossEnemy(enemyTexture, x, y); break;
//            case 2: newEnemy = new ?Enemy(enemyTexture, x, y); break;
//            case 3: newEnemy = new ?Enemy(enemyTexture, x, y); break;
//            case 4: newEnemy = new ?Enemy(enemyTexture, x, y); break;
            default: newEnemy = new BasicEnemy(enemyTexture, x, y); break;
        }
        if(waveDifficulty < currentValue + newEnemy.getValue()){
            newEnemy = null;
        }
        else {
            if(waveDifficulty == currentValue + newEnemy.getValue()){
                waveFull = true;
            }
            currentValue += newEnemy.getValue();
            enemies.add(newEnemy);
        }
    }

    private void endWave() {
        inShopPhase = true;
        System.out.println("Wave " + currentWave + " survived! Entering Shop Phase.");
    }

    public void startNextWave() {
        currentWave++;
        waveDifficulty = (int) (waveDifficulty * 1.4);
//        waveTimer = 0f;
        this.currentValue = 0;
        inShopPhase = false;
        waveFull = false;
        System.out.println("Starting Wave " + currentWave + ".");
    }

    public boolean isInShopPhase() {
        return inShopPhase;
    }
}
