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
    int salto = 1;
    int gravedad = 9;
    Texture pinyaTexture;
    SpriteBatch spriteBatch;
    Viewport viewport;
    Camera camera;
    Sprite pinya;


    @Override
    public void create () {
        pinyaTexture = new Texture("images.jpg");
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
        if (Gdx.input.isButtonJustPressed(Input.Keys.W)){

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
