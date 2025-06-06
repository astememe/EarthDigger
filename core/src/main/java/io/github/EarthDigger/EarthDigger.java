package io.github.EarthDigger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EarthDigger extends ApplicationAdapter implements ApplicationListener {
    Array<Rectangle> bloques;

    float delta;
    int screenSizeX = 320;
    int screensizeY = 48;
    float groundY = 175;
    boolean paused = true;
    Viewport viewport;
    OrthographicCamera camera;
    Personaje personaje;
    SpriteBatch spriteBatch;
    //Sprite dirt;
    //Sprite background;
    Rectangle personajeHitBox;
    //Rectangle bloqueHitBox;
    GameMap gameMap;

    @Override
    public void create () {
        bloques = new Array<>();

        TiledMap map = new TmxMapLoader().load("MapaEarthDigger.tmx");
        MapLayer capaColisiones = map.getLayers().get("colisiones");

        if (capaColisiones != null) {
            for (MapObject objeto : capaColisiones.getObjects()) {
                if (objeto instanceof RectangleMapObject) {
                    Rectangle rect = ((RectangleMapObject) objeto).getRectangle();
                    bloques.add(rect);
                }
            }
        }

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        viewport = new ExtendViewport(screenSizeX, screensizeY, camera);
        spriteBatch = new SpriteBatch();

        Assets.loadTextures(groundY);
        gameMap = new TiledGameMap();
        personaje = Assets.personaje;
        //background = Assets.background;
        //dirt = Assets.dirt;
        personajeHitBox = Assets.personajeHitBox;
        //bloqueHitBox = Assets.bloqueHitBox;
    }



    @Override
    public void render () {
        delta = Gdx.graphics.getDeltaTime(); // 0.0167 = 60 FPS
        draw();
        logic();
        if (paused) {
            if (Gdx.input.isKeyPressed(Input.Keys.P)) {
                paused = false;
            }
        }
        System.out.println(gameMap.getLayers().getCount());


        gameMap.render(camera);

        //System.out.println(delta);
        //System.out.println(bloqueHitBox.overlaps(personajeHitBox));
        //System.out.println(personaje.getY());

    }

    public void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        personajeHitBox.setPosition(personaje.getX(), personaje.getY());



        // POSICION DE CÁMARA
        camera.position.x = personaje.getSprite().getX() + personaje.getSprite().getWidth() / 2f;
        camera.position.y = personaje.getSprite().getY() + personaje.getSprite().getHeight() / 2f;

        // POSICION DE CÁMARA FIJA CUANDO LLEGA AL BORDE DE LA PANTALLA
        if (camera.position.x < viewport.getWorldWidth() / 2f) {
            camera.position.x = viewport.getWorldWidth() / 2f;
        }
        //if (camera.position.x > background.getX() + background.getWidth() - viewport.getWorldWidth() / 2f) {
        //    camera.position.x = background.getX() + background.getWidth() - viewport.getWorldWidth() / 2f;
        //}

        camera.update();

        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        //CARGAR SPRITES
        spriteBatch.begin();
        //background.draw(spriteBatch);
        personaje.dibujar(spriteBatch);


        //for (int i = 0; i < background.getWidth(); i+=16) {
        //    dirt.draw(spriteBatch);
        //    dirt.setPosition(i, groundY-16);
        //}
        spriteBatch.end();
    }

    public void logic() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            personaje.moverIzquierda(delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            personaje.moverDerecha(delta);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            personaje.saltar();
        }

        personaje.reiniciarSaltos(delta, bloques);

        for (Rectangle bloque : bloques) {
            if (bloque.overlaps(personajeHitBox)) {
                System.out.println("¡Colisión detectada!");
                // Aquí puedes frenar el movimiento, aplicar lógica de colisión, etc.
            }
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
        personaje.dispose();
        spriteBatch.dispose();
    }
}
