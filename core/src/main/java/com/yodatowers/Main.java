package com.yodatowers;

import com.badlogic.gdx.Game;
import com.yodatowers.ui.MainMenu;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    @Override
    public void create () {
        setScreen(new MainMenu(Main.this)); // calls MainMenu
    }
}
