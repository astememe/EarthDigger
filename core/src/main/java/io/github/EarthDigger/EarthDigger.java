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
    float delta;
    int screenSizeX = 320;
    int screensizeY = 48;
    float velocidadY = 0;
    float gravedad = -100;
    boolean saltando = false;
    float groundY = 30; // Cambiar por posición del bloque
    Texture dirtTexture;
    Sprite dirt;
    Texture backgroundTexture;
    Texture personajeTexture;
    SpriteBatch spriteBatch;
    Viewport viewport;
    OrthographicCamera camera;
    Sprite personaje;
    Sprite background;


    @Override
    public void create () {
        backgroundTexture = new Texture("Flux_Dev_A_sweeping_cinematic_landscape_with_dramatic_clouds_t_3.jpg");
        background = new Sprite(backgroundTexture);
        background.setSize(1600,800);
        background.setPosition(0, -groundY*2);
        personajeTexture = new Texture("PERSONAJE PRUEBA.png");
        personaje = new Sprite(personajeTexture);
        personaje.setSize(16,16);
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(screenSizeX, screensizeY, camera);
        spriteBatch = new SpriteBatch();

        dirtTexture = new Texture("césped.png");
        dirt = new Sprite(dirtTexture);
        dirt.setSize(16,16);


    }

    @Override
    public void render () {
        delta = Gdx.graphics.getDeltaTime(); // 0.0167 = 60 FPS
        draw();
        logic();
        System.out.println(delta);

    }

    public void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();

        // POSICION DE CÁMARA
        camera.position.x = personaje.getX() + personaje.getWidth() / 2f;
        camera.position.y = personaje.getHeight() / 2f + personaje.getY();

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
        personaje.draw(spriteBatch);
        for (int i = 0; i < background.getWidth(); i+=16) {
            dirt.draw(spriteBatch);
            dirt.setPosition(i, groundY-16);
        }
        spriteBatch.end();
    }

    public void logic() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            personaje.setX(personaje.getX() - delta*20);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            personaje.setX(personaje.getX() + delta*20);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) && !saltando || Gdx.input.isKeyJustPressed(Input.Keys.UP) && !saltando){
            velocidadY = 50;
            saltando = true;
        }

        velocidadY += gravedad * delta; // -100 * 0.016 -> velocidadY = -1.67 (Disminuye la vecolidad por cada frame que pasa hasya llegar a 0)
        personaje.setY(personaje.getY() + velocidadY * delta); // Cuando se resta y llega a 0, la posición de Y se le resta para que baje haciendo que sea negatico y logre bajar.)

        if (personaje.getY() <= groundY) {
            personaje.setY(groundY);
            velocidadY = 0;
            saltando = false;
        }
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
