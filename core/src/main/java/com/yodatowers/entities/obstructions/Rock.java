package com.yodatowers.entities.obstructions;

import com.badlogic.gdx.graphics.Texture;

public class Rock extends Obstruction{
    public Rock(Texture texture, float x, float y){
        super(texture, x, y, 3/4f, 3/4f);
    }

}
