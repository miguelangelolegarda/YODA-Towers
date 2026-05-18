package com.yodatowers;

import com.badlogic.gdx.Game;
import com.yodatowers.UI.MainMenu;

public class Main extends Game {

    @Override
    public void create () {
        setScreen(new MainMenu(Main.this)); // calls MainMenu
        
    }

}