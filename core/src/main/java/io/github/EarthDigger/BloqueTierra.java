package io.github.EarthDigger;

import com.badlogic.gdx.graphics.Texture;

public class BloqueTierra extends Bloque {
    private int id = 1;

    public BloqueTierra(float x, float y, float width, float height, Texture textura) {
        super(x, y, width, height, textura);
    }

    public int getId() {
        return id;
    }
}
