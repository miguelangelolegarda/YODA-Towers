package com.yodatowers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yodatowers.logic.*;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game implements ApplicationListener {
    Texture backgroundTexture;
    Texture yodaTexture;
    Texture palpTexture;
    Texture saberTexture;
    Sound yodaDeathSound;
    Sound palpDeathSound;
    SpriteBatch spriteBatch;
    Sprite yodaSprite;
    FitViewport viewport;
    Array<Enemy> enemies;
    Array<Sprite> lightsabers;
    float spawnTimer;
    float saberTimer;
    Rectangle yodaRectangle;
    Rectangle saberRectangle;


    @Override
    public void create() {
        backgroundTexture = new Texture("background.jpg");
        yodaTexture = new Texture("legoYoda.png");
        palpTexture = new Texture("palpatine.png");
        saberTexture = new Texture("greenSaber.png");
        yodaDeathSound = Gdx.audio.newSound(Gdx.files.internal("lego-yoda-death-sound-effect.mp3"));
        palpDeathSound = Gdx.audio.newSound(Gdx.files.internal("lego-star-wars-palpatine-hurt-sound.mp3"));

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);

        yodaSprite = new Sprite(yodaTexture);
        yodaSprite.setSize(1/2f, 3/4f);
        yodaSprite.setPosition((viewport.getWorldWidth()/2f) - 1/4f, (viewport.getWorldHeight()/2f) - 3/8f);
        yodaSprite.setOriginCenter();
        enemies = new Array<>(); // Changed
        lightsabers = new Array<>();
        yodaRectangle = new Rectangle();
        saberRectangle = new Rectangle();
    }

    @Override
    public void render() {
        // Draw your screen here. "delta" is the time since last render in seconds.
        input();
        logic();
        draw();
    }

    private void input(){
        float speed = 3f;
        float delta = Gdx.graphics.getDeltaTime();
//        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
//            yodaSprite.rotate(-speed*delta);
//        } else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
//            yodaSprite.rotate(speed*delta);
//        }

        // Logic to move Yoda Sprite
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            yodaSprite.translateY(speed * delta);
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            yodaSprite.translateY(-speed * delta);
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            yodaSprite.translateX(-speed * delta);
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            yodaSprite.translateX(speed * delta);


        float maxX = viewport.getWorldWidth() - yodaSprite.getWidth();
        float maxY = viewport.getWorldHeight() - yodaSprite.getHeight();

        float clampedX = MathUtils.clamp(yodaSprite.getX(), 0, maxX);
        float clampedY = MathUtils.clamp(yodaSprite.getY(), 0, maxY);

        yodaSprite.setPosition(clampedX, clampedY);

        Vector2 mousePos = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
        float angle = MathUtils.atan2(mousePos.y - yodaSprite.getY(), mousePos.x - yodaSprite.getX()) * MathUtils.radiansToDegrees;
        yodaSprite.setRotation(angle);
    }

    private void logic(){
        float delta = Gdx.graphics.getDeltaTime();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float palpSpeed = 2f;
        float saberSpeed = 10f;
        yodaRectangle.set(yodaSprite.getX(),yodaSprite.getY(),yodaSprite.getWidth()/2,yodaSprite.getHeight()/2);
        // Enemy movement
        for (int j = enemies.size - 1; j >= 0; j--) {
            Enemy enemy = enemies.get(j);
            enemy.update(delta, yodaSprite.getX(), yodaSprite.getY());
        }
        // Projectile movement and collision checks
        for(int i = lightsabers.size-1;i>=0;i--) {
            Sprite saberSprite = lightsabers.get(i);
            float angleRadians = (float) Math.toRadians(saberSprite.getRotation());
            float xTrajectory = saberSpeed * (float) Math.cos(angleRadians) * delta;
            float yTrajectory = saberSpeed * (float) Math.sin(angleRadians) * delta;
            saberSprite.translate(xTrajectory, yTrajectory);
            float xVal = saberSprite.getX();
            float yVal = saberSprite.getY();
            float saberHeight = saberSprite.getHeight();
            float saberWidth = saberSprite.getWidth();
            saberRectangle.set(xVal, yVal, saberWidth, saberHeight);
            if (xVal < -saberWidth || yVal < -saberHeight || xVal > worldWidth + saberWidth || yVal > worldHeight + saberHeight) {
                lightsabers.removeIndex(i);
                continue; // Skip collision check if already removed
            }
            for (int j = enemies.size - 1; j >= 0; j--) {
                Enemy enemy = enemies.get(j);

                if (yodaRectangle.overlaps(enemy.getBounds())) {
                    enemies.removeIndex(j);
                    yodaDeathSound.play();
                    break;
                } else if (saberRectangle.overlaps(enemy.getBounds())) {
                    lightsabers.removeIndex(i); // Destroy Saber
                    enemy.takeDamage(1); // Hurt Enemy

                    if(enemy.isDead()) {
                        enemies.removeIndex(j); // Kill Enemy if HP = 0
                        palpDeathSound.play();
                    }
                    break;
                }
            }
        }
        //spawn and fire rates
        spawnTimer += delta;
        saberTimer += delta;
        if(spawnTimer > 1f){
            spawnTimer = 0;
            spawnEnemy();
        }
        if(saberTimer > 0.3f){
            saberTimer = 0;
            spawnSabers();
        }
    }

    private void draw(){
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        yodaSprite.draw(spriteBatch);

        for(Enemy enemy : enemies){
            enemy.draw(spriteBatch);
        }
        for(Sprite saberSprite : lightsabers){
            saberSprite.draw(spriteBatch);
        }
        spriteBatch.end();
    }

    private void spawnSabers(){
        Sprite saberSprite = new Sprite(saberTexture);
        saberSprite.setSize(1/13f,1/2f);
        saberSprite.setOriginCenter();
        saberSprite.rotate(yodaSprite.getRotation());
        saberSprite.setX(yodaSprite.getX());
        saberSprite.setY(yodaSprite.getY());
        lightsabers.add(saberSprite);
    }

    private void spawnEnemy(){
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float screenSide = MathUtils.random(0,3);
        float x = 0;
        float y = 0;
        float offScreenBuffer = 5;

        switch((int)screenSide){
            case 0:
                x = MathUtils.random(0, worldWidth);
                y = worldHeight + offScreenBuffer;
                break;
            case 1:
                x = -offScreenBuffer;
                y = MathUtils.random(0, worldHeight);
                break;
            case 2:
                x = MathUtils.random(0, worldWidth);
                y = -offScreenBuffer;
                break;
            case 3:
                x = worldWidth + offScreenBuffer;
                y = MathUtils.random(0, worldHeight);
                break;
        }

        // Randomly spawn 1 of the 5 enemy variants
        int enemyType = MathUtils.random(0, 4);
        Enemy newEnemy;

        switch(enemyType) {
            case 0: newEnemy = new BasicEnemy(palpTexture, x, y); break;
            case 1: newEnemy = new BossEnemy(palpTexture, x, y); break;
//            case 2: newEnemy = new Enemyvar2(palpTexture, x, y); break;
//            case 3: newEnemy = new Enemyvar3(palpTexture, x, y); break;
//            case 4: newEnemy = new Enemyvar4(palpTexture, x, y); break;
            default: newEnemy = new BasicEnemy(palpTexture, x, y); break;
        }

        enemies.add(newEnemy);
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;
        viewport.update(width, height, true);
        // Resize your screen here. The parameters represent the new window size.
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
    public void dispose() {
        spriteBatch.dispose();
        palpTexture.dispose();
    }
}
