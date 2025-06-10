package io.github.EarthDigger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.Random;

public class Assets {
    public static Texture cespedTexture;
    public static Texture tierraTexture;
    public static Texture piedraTexture;
    public static Texture vidaTexture;
    public static BitmapFont font;

    public static void load() {
        cespedTexture = new Texture(Gdx.files.internal("cespedfinal.png"));
        tierraTexture = new Texture(Gdx.files.internal("bloquetierra.png"));
        piedraTexture = new Texture(Gdx.files.internal("piedraFINAL.png"));
        vidaTexture = new Texture(Gdx.files.internal("corazonTexture.png"));
        font = new BitmapFont();
        font.setColor(new Color(Color.RED));
    }

    public static void dispose() {
        cespedTexture.dispose();
        tierraTexture.dispose();
        piedraTexture.dispose();
        font.dispose();
    }
}
