package io.github.EarthDigger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class EarthDigger extends ApplicationAdapter {
    int screenSizeX = 50;
    int screensizeY = 20;
    Texture backgroundTexture;
    Texture pinyaTexture;
    SpriteBatch spriteBatch;
    Viewport viewport;
    OrthographicCamera camera;
    Sprite pinya;
    Sprite background;

    @Override
    public void create () {
        backgroundTexture = new Texture("frutas.jpg");
        background = new Sprite(backgroundTexture);
        background.setSize(100,90);
        background.setPosition(-20, -20);
        pinyaTexture = new Texture("images.jpg");
        pinya = new Sprite(pinyaTexture);
        pinya.setSize(3,5);
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(70, 50, camera);
        camera.position.x=pinya.getX()+ pinya.getHeight()/2f;
        camera.position.y=pinya.getY()+ pinya.getWidth()/2f;
        camera.update();
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void render () {
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            pinya.setX(pinya.getX() - (float)0.5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            pinya.setX(pinya.getX() + (float)0.5);
        }
        camera.position.x=pinya.getX()+ pinya.getHeight()/2f;
        camera.position.y=pinya.getY()+pinya.getWidth()/2f;
        camera.update();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);


        spriteBatch.begin();
        background.draw(spriteBatch);
        pinya.draw(spriteBatch);
        spriteBatch.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.update();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
    }
}
