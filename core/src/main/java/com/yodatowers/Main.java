package com.yodatowers;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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

/** Main application entry. Shows a start button and switches to GameScreen on click. */
public class Main extends Game {
    private Stage stage;
    private Table table;
    private Texture buttonTexture;
    private BitmapFont buttonFont;
    private TextButton startButton;

    @Override
    public void create () {
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
                setScreen(new GameScreen(Main.this));
            }
        });
        table.add(startButton).pad(100f);
    }

    @Override
    public void render () {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
        stage.act(Gdx.graphics.getDeltaTime()); // Update UI
        stage.draw(); // render UI
        super.render(); // render current screen if any
    }

    @Override
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
        if (getScreen() != null) getScreen().resize(width, height);
    }

    @Override
    public void dispose() { // Frees memory when closing the game
        if (stage != null) stage.dispose();
        if (buttonTexture != null) buttonTexture.dispose();
        if (buttonFont != null) buttonFont.dispose();
        if (getScreen() != null) getScreen().dispose();
    }
}
