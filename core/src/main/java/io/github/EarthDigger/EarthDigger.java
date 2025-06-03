package io.github.EarthDigger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class EarthDigger extends ApplicationAdapter {
    int screenSizeX = 50;
    int screensizeY = 40;
    float velocidadY = 0;
    float gravedad = -100;
    boolean saltando = false;
    float groundY = 5;
    Texture pinyaTexture;
    SpriteBatch spriteBatch;
    Viewport viewport;
    Camera camera;
    Sprite pinya;


    @Override
    public void create () {
        pinyaTexture = new Texture("piña_ttrans.png");
        pinya = new Sprite(pinyaTexture);
        pinya.setSize(3,5);
        camera = new OrthographicCamera();
        viewport = new FitViewport(screenSizeX, screensizeY, camera);
        camera.position.x=screenSizeX/2f;
        camera.position.y=screensizeY/2f;
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void render () {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        pinya.draw(spriteBatch);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            pinya.setX(pinya.getX() - (float)0.5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            pinya.setX(pinya.getX() + (float)0.5);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && !saltando) {
            velocidadY = 50;
            saltando = true;
        }

        float delta = Gdx.graphics.getDeltaTime(); // 0.0167 = 60 FPS
        velocidadY += gravedad * delta; // -100 * 0.016 -> velocidadY = -1.67 (Disminuye la vecolidad por cada frame que pasa hasya llegar a 0)
        pinya.setY(pinya.getY() + velocidadY * delta); // Cuando se resta y llega a 0, la posición de Y se le resta para que baje haciendo que sea negatico y logre bajar.)

        if (pinya.getY() <= groundY) {
            pinya.setY(groundY);
            velocidadY = 0;
            saltando = false;
        }

        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
