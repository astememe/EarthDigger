package io.github.EarthDigger;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Bloque extends Rectangle {
    Texture textura;

    public Bloque(float x, float y, float width, float height, Texture textura) {
        super(x, y, width, height);
        this.textura = textura;
    }

    public Texture getTextura() {
        return textura;
    }
}
