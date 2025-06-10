package io.github.EarthDigger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public static Texture cespedTexture;
    public static Texture tierraTexture;
    public static Texture piedraTexture;
    public static Texture vidaTexture;

    public static void load() {
        cespedTexture = new Texture(Gdx.files.internal("cespedfinal.png"));
        tierraTexture = new Texture(Gdx.files.internal("bloquetierra.png"));
        piedraTexture = new Texture(Gdx.files.internal("piedraFINAL.png"));
        vidaTexture = new Texture(Gdx.files.internal("corazonTexture.png"));
    }

    public static void dispose() {
        cespedTexture.dispose();
        tierraTexture.dispose();
        piedraTexture.dispose();
    }
}
