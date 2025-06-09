package io.github.EarthDigger;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Bloque extends Rectangle {
    Texture textura;
    private float[] coordenadas = new float[2];

    public Bloque(float x, float y, float width, float height, Texture textura) {
        super(x, y, width, height);
        this.textura = textura;
        setCoordenadas(new float[]{x, y});
    }



    public Texture getTextura() {
        return textura;
    }

    public float[] getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(float[] coordenadas) {
        this.coordenadas = coordenadas;
    }
}
