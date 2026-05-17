package com.yodatowers.UI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yodatowers.logic.GameScreen;

public class Loading implements Screen{
    
    private final Game game; 
    private float currentLoadingTime = 0f; // store how long loading screen has been shown
    private final float loadingTime = 5f; // Fixed time for loading
    
    private float accumulator = 0f;
    private final float step = 1f; // set output of progress time | limit message spamming

    private String loadingText = "Loading: 0%";
    // for display output
    SpriteBatch batch;
    BitmapFont font;

    private boolean paused = false;

    public Loading(Game game) {
        this.game = game;
        batch = new SpriteBatch();
        font = new BitmapFont();
    }

    public boolean update(float delta){
        if (paused) return false; // do not update while paused

        currentLoadingTime += delta; //update loading time

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f,1); // set bg 
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); 

        // get loading progress
        float progress = Math.min(currentLoadingTime / loadingTime, 1f); 
        boolean finished = progress >= 1f; // finished when progress reaches 100%
        
        accumulator += delta;
        if (accumulator >= step){ // print text based on step
            loadingText = "Loading: " + (int)(progress * 100) + "%";
            System.out.println(loadingText); //REMOVE AFTER TESTING
            accumulator -= step;
        }

        batch.begin();
        font.draw(batch, loadingText, 100, 200);
        batch.end();

        if (finished){
            loadingText = "Loading Complete";
            System.out.println("Loading Complete"); //REMOVE AFTER TESTING
        }
        
        return finished;
    }

    @Override
    public void render(float delta) {
        // call update every frame. if finished, switch to the GameScreen
        boolean finished = update(delta);
        if (finished) {
            game.setScreen(new GameScreen());
        }
    }

    @Override
    public void show() {
        // reset loading state when shown
        currentLoadingTime = 0f;
        accumulator = 0f;
        loadingText = "Loading: 0%";
        paused = false;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void hide() {
        // Called when this screen is no longer the current screen.
        dispose();
    }

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (font != null) {
            font.dispose();
        }
    }
}
