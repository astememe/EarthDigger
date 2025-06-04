package io.github.EarthDigger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class EarthDigger extends ApplicationAdapter implements ApplicationListener {
    int screenSizeX = 100;
    int screensizeY = 100;
    float velocidadY = 0;
    float gravedad = -100;
    boolean saltando = false;
    float groundY = 5; // Cambiar por posición del bloque
    Texture backgroundTexture;
    Texture pinyaTexture;
    SpriteBatch spriteBatch;
    Viewport viewport;
    OrthographicCamera camera;
    Sprite pinya;
    Sprite background;
    float DeltaTime = 0;

    @Override
    public void create () {
        float DeltaTime = 0;
        backgroundTexture = new Texture("césped.png");
        background = new Sprite(backgroundTexture);
        background.setSize(400,100);
        background.setPosition(0, -20);
        pinyaTexture = new Texture("HACHA.png");
        pinya = new Sprite(pinyaTexture);
        pinya.setSize(3,5);
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(screenSizeX, screensizeY, camera);
        spriteBatch = new SpriteBatch();
    }

    @Override
    public void render () {
        draw();

        float delta = Gdx.graphics.getDeltaTime(); // 0.0167 = 60 FPS

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pinya.setX(pinya.getX() - delta*20);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            pinya.setX(pinya.getX() + delta*20);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && !saltando || Gdx.input.isKeyJustPressed(Input.Keys.UP) && !saltando){
            velocidadY = 50;
            saltando = true;
        }

        velocidadY += gravedad * delta; // -100 * 0.016 -> velocidadY = -1.67 (Disminuye la vecolidad por cada frame que pasa hasya llegar a 0)
        pinya.setY(pinya.getY() + velocidadY * delta); // Cuando se resta y llega a 0, la posición de Y se le resta para que baje haciendo que sea negatico y logre bajar.)

        if (pinya.getY() <= groundY) {
            pinya.setY(groundY);
            velocidadY = 0;
            saltando = false;
        }
        System.out.println(delta);

    }

    public void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();

        // POSICION DE CÁMARA
        camera.position.x = pinya.getX() + pinya.getWidth() / 2f;
        camera.position.y = pinya.getY() + pinya.getHeight() / 2f + 15f;

        // POSICION DE CÁMARA FIJA CUANDO LLEGA AL BORDE DE LA PANTALLA
        if (camera.position.x < viewport.getWorldWidth() / 2f) {
            camera.position.x = viewport.getWorldWidth() / 2f;
        }
        if (camera.position.x > background.getX() + background.getWidth() - viewport.getWorldWidth() / 2f) {
            camera.position.x = background.getX() + background.getWidth() - viewport.getWorldWidth() / 2f;
        }
        camera.update();

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        //CARGAR SPRITES
        spriteBatch.begin();
        background.draw(spriteBatch);
        pinya.draw(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

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
