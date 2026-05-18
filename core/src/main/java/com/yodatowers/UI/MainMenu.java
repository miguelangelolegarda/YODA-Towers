package com.yodatowers.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.yodatowers.Main;


//shows start button
public class MainMenu implements Screen {
    private final Main game;
    private Stage stage;
    private Table table;
    private Texture buttonTexture;
    private BitmapFont buttonFont;
    private TextButton startButton;

    public MainMenu(Main game) {
        this.game = game;
    }

    @Override
    public void show () {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        buttonFont = new BitmapFont();

        table = new Table();
        table.setFillParent(true); // take up the whole screen
        table.center();
        stage.addActor(table);

        table.setDebug(false);

        // Create pixel
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        buttonTexture = new Texture(pixmap);
        pixmap.dispose();

        // Button for Start Game
        TextButtonStyle style = new TextButtonStyle();
        style.font = buttonFont;
        TextureRegionDrawable buttonBackground = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        style.up = buttonBackground.tint(new Color(0.85f, 0.85f, 0.85f, 1f)); // normal
        style.over = buttonBackground.tint(new Color(0.95f, 0.95f, 0.95f, 1f)); // hover
        style.down = buttonBackground.tint(new Color(0.70f, 0.70f, 0.70f, 1f)); // click
        style.fontColor = Color.BLACK;
        startButton = new TextButton("Start Game", style);
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                // Switch to the Loading screen then the Game Screen
                game.setScreen(new Loading(game));
            }
        });
        table.add(startButton).pad(100f);
    }

    @Override
    public void render (float delta) {
        Gdx.gl.glClearColor(0.12f, 0.12f, 0.14f, 1f); //set bg color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() { // Frees memory when closing the game
        if (stage != null) stage.dispose();
        if (buttonTexture != null) buttonTexture.dispose();
        if (buttonFont != null) buttonFont.dispose();
    }

    @Override
    public void pause(){

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }





}
