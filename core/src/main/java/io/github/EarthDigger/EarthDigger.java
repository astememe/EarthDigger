package io.github.EarthDigger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Vector;

public class EarthDigger extends ApplicationAdapter {
    ArrayList<Bloque> bloques;
    Mapa mapa;
    Enemy enemy;
    int[][] mapa_forma;
    Vector3 mouse_position = new Vector3(0,0,0);
    Vector3 mouse_snapshot = new Vector3(0,0,0);
    int[] true_mouse_position = new int[2];
    float delta;
    int screenSizeX = 320;
    int screenSizeY = 48;
    Viewport viewport;
    OrthographicCamera camera;
    Personaje personaje;
    int mapWidth;

    SpriteBatch spriteBatch;

    @Override
    public void create() {
        mapa = new Mapa();
        mapa_forma = mapa.getForma();
        mapWidth = mapa.getForma()[0].length;
        bloques = new ArrayList<>();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ExtendViewport(screenSizeX, screenSizeY, camera);

        spriteBatch = new SpriteBatch();

        Assets.load();

        mapa.rellenarMapa(bloques);
        enemy = new Enemy("easteregg.png", 16, 16);
        personaje = new Personaje("Frames.png", 16, 16);

    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.WHITE);
        delta = Gdx.graphics.getDeltaTime();
        mapa.rellenarMapa(bloques);

        enemy.seguirAlJugador(personaje.getPosicion(), delta);
        enemy.update(delta);


        personaje.reiniciarSaltos(delta, bloques);
        personaje.update(delta);
        logic();

        spriteBatch.begin();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        for (Bloque bloque : bloques) {
            spriteBatch.draw(bloque.getTextura(), bloque.x, bloque.y, bloque.width, bloque.height);
        }
        personaje.dibujar(spriteBatch);
        enemy.dibujar(spriteBatch);
        spriteBatch.end();

        viewport.apply();

        camera.update();
    }

    public void logic() {
        //CAMARA
        camera.position.x = personaje.getX() + personaje.getAncho() / 2f;
        camera.position.y = personaje.getY() + personaje.getAlto() / 2f;

        if (camera.position.x < viewport.getWorldWidth() / 2f) {
            camera.position.x = viewport.getWorldWidth() / 2f;
        } else if (camera.position.x > mapWidth*16 - viewport.getWorldWidth() / 2f) {
            camera.position.x = mapWidth*16 - viewport.getWorldWidth() / 2f;
        }

        if (camera.position.y < viewport.getWorldHeight() / 2f) {
            camera.position.y = viewport.getWorldHeight() / 2f - 16 * (mapa.getForma().length - 1);
        }


        //INPUTS
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }


        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                personaje.moverIzquierda(delta * 2);
            } else {
                personaje.moverIzquierda(delta);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                personaje.moverDerecha(delta * 2);
            } else {
                personaje.moverDerecha(delta);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            personaje.saltar();
        }

        //POSICION RATON
        mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse_position);
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            mouse_snapshot = mouse_position;
            true_mouse_position[0] = (int)mouse_snapshot.x/16;
            if (mouse_snapshot.y > 0) {
                true_mouse_position[1] = (int)mouse_snapshot.y/16;
            } else {
                true_mouse_position[1] = (int)mouse_snapshot.y/16 - 1;
            }
            System.out.println(true_mouse_position[0] + ", " + true_mouse_position[1]);
            if (-true_mouse_position[1] != mapa_forma.length - 1) {
                mapa_forma[-true_mouse_position[1]][true_mouse_position[0]] = 0;
                mapa.setForma(mapa_forma);
            }
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            mouse_snapshot = mouse_position;
            true_mouse_position[0] = (int)mouse_snapshot.x/16;
            if (mouse_snapshot.y > 0) {
                true_mouse_position[1] = (int)mouse_snapshot.y/16;
            } else {
                true_mouse_position[1] = (int)mouse_snapshot.y/16 - 1;
            }
            System.out.println(true_mouse_position[0] + ", " + true_mouse_position[1]);
            if (-true_mouse_position[1] != mapa_forma.length - 1) {
                mapa_forma[-true_mouse_position[1]][true_mouse_position[0]] = 1;
                mapa.setForma(mapa_forma);
            }
        }

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
        Assets.dispose();
    }
}
