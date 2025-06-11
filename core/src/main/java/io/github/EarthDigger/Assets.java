package io.github.EarthDigger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Random;

public class Assets {
    public static Texture cespedTexture;
    public static Texture tierraTexture;
    public static Texture piedraTexture;
    public static Texture vidaTexture;
    public static BitmapFont font;
    private static Animation<TextureRegion> enemy2WalkRight;

    public static Animation<TextureRegion> getEnemy2WalkRight() {
        if (enemy2WalkRight == null) {
            Texture spriteSheet = new Texture(Gdx.files.internal("PERSONAJEACTUALIZADO/Frames.png"));
            TextureRegion[][] tmp = TextureRegion.split(spriteSheet, 32, 16);
            TextureRegion[] frames = new TextureRegion[2];
            frames[0] = tmp[0][0];
            frames[1] = tmp[0][1];
            enemy2WalkRight = new Animation<>(0.5f, frames);
        }
        return enemy2WalkRight;
    }

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
