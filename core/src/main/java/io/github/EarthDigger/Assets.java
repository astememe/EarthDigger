package io.github.EarthDigger;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Assets {

    public static Texture backgroundTexture;
    public static Sprite background;
    public static Texture dirtTexture;
    public static Sprite dirt;
    public static Personaje personaje;
    public static Rectangle personajeHitBox;
    public static Rectangle bloqueHitBox;

    public static float groundY;

    public static void loadTextures(float groundYParam) {
        groundY = groundYParam;

        // Textura del fondo
        backgroundTexture = new Texture("Flux_Dev_A_sweeping_cinematic_landscape_with_dramatic_clouds_t_3.jpg");
        background = new Sprite(backgroundTexture);
        background.setSize(1600, 800);
        background.setPosition(0, -groundY * 2);

        // Textura del personaje
        personaje = new Personaje("PERSONAJEQUIETOEXHALAR.png", 16, 16);
        personajeHitBox = new Rectangle(personaje.getX(), personaje.getY(), 16, 16);

        // Textura de tierra
        dirtTexture = new Texture("cespedfinal.png");
        dirt = new Sprite(dirtTexture);
        dirt.setSize(16, 16);
        bloqueHitBox = new Rectangle(personajeHitBox.x, groundY - 15, 16, 16);
    }
}
