package com.yodatowers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameScreen implements Screen { // IMPLEMENTS SCREEN
    Texture backgroundTexture;
    Texture yodaTexture;
    Texture palpTexture;
    Texture saberTexture;
    Sound yodaDeathSound;
    Sound palpDeathSound;
    SpriteBatch spriteBatch;
    Sprite yodaSprite;
    FitViewport viewport;
    Array<Sprite> palpatines;
    Array<Sprite> lightsabers;
    float spawnTimer;
    float saberTimer;
    Rectangle yodaRectangle;
    Rectangle saberRectangle;
    Rectangle palpRectangle;

    // CONSTRUCTOR
    private Game game;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() { // CONVERT CREATE TO SHOW
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
        palpatines = new Array<>();
        lightsabers = new Array<>();
        yodaRectangle = new Rectangle();
        saberRectangle = new Rectangle();
        palpRectangle = new Rectangle();
    }

    @Override
    public void render(float delta) {
        // Draw your screen here. "delta" is the time since last render in seconds.
        input(delta);
        logic(delta);
        draw();
    }

    private void input(float delta){
        float speed = 180f;
        
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            yodaSprite.rotate(-speed*delta);
        } else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            yodaSprite.rotate(speed*delta);
        }
    }

    private void logic(float delta){
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float palpSpeed = 2f;
        float saberSpeed = 10f;
        yodaRectangle.set(yodaSprite.getX(),yodaSprite.getY(),yodaSprite.getWidth()/2,yodaSprite.getHeight()/2);
        //palp movement
        for (int j = palpatines.size - 1; j >= 0; j--) {
            Sprite palpSprite = palpatines.get(j);
            float xMovement = yodaSprite.getX() - palpSprite.getX();
            float yMovement = yodaSprite.getY() - palpSprite.getY();
            float len = (float)Math.sqrt(xMovement*xMovement + yMovement*yMovement);
            if (len != 0){
                xMovement /= len;
                yMovement /= len;
            }
            palpSprite.translate(xMovement*palpSpeed*delta, yMovement*palpSpeed*delta);
        }
        //projectile movement and collision checks
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
            }
            for (int j = palpatines.size - 1; j >= 0; j--) {
                Sprite palpSprite = palpatines.get(j);
                palpRectangle.set(palpSprite.getX(), palpSprite.getY(), palpSprite.getWidth(), palpSprite.getHeight());

                if (yodaRectangle.overlaps(palpRectangle)) {
                    palpatines.removeIndex(j);
                    yodaDeathSound.play();
                    break;
                } else if (saberRectangle.overlaps(palpRectangle)) {
                    palpatines.removeIndex(j);
                    lightsabers.removeIndex(i);
                    palpDeathSound.play();
                    break;
                }
            }
        }
        //spawn and fire rates
        spawnTimer += delta;
        saberTimer += delta;
        if(spawnTimer > 1f){
            spawnTimer = 0;
            spawnPalps();
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

        for(Sprite palpSprite : palpatines){
            palpSprite.draw(spriteBatch);
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

    private void spawnPalps(){
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        float palpheight = 3/4f;
        float palpwidth = 3/5f;
        float screenSide = MathUtils.random(0,3);
        float x = 0;
        float y = 0;
        float offScreenBuffer = 5;

        Sprite palpSprite = new Sprite(palpTexture);
        palpSprite.setSize(palpwidth, palpheight);
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
        palpSprite.setX(x);
        palpSprite.setY(y);
        palpatines.add(palpSprite);
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
    public void hide() {
        // Called when this screen is no longer the current screen.
        dispose();
    }

    @Override
    public void dispose() {
        if (spriteBatch != null) spriteBatch.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (yodaTexture != null) yodaTexture.dispose();
        if (palpTexture != null) palpTexture.dispose();
        if (saberTexture != null) saberTexture.dispose();
        if (yodaDeathSound != null) yodaDeathSound.dispose();
        if (palpDeathSound != null) palpDeathSound.dispose();
    }
}
