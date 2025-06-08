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

import java.util.Vector;

public class EarthDigger extends ApplicationAdapter {
    Array<Bloque> bloques;
    Mapa mapa;
    Vector3 mouse_position = new Vector3(0,0,0);
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
        mapWidth = mapa.getForma()[0].length;
        bloques = new Array<>();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ExtendViewport(screenSizeX, screenSizeY, camera);

        spriteBatch = new SpriteBatch();

        Assets.load();

        // Generar bloques con clase Bloque y texturas de Assets
        //int[][] mapa_forma = mapa.getForma();
        //mapa_forma[1][0] = 0;
        //mapa.setForma(mapa_forma);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (mapa.getForma()[i][j] == 1) {
                    bloques.add(new Bloque(j*16, i*-16, 16, 16, Assets.cespedTexture));
                } else if (mapa.getForma()[i][j] == 2) {
                    bloques.add(new Bloque(j*16, i*-16, 16, 16, Assets.tierraTexture));
                } else if (mapa.getForma()[i][j] == 3) {
                    bloques.add(new Bloque(j*16, i*-16, 16, 16, Assets.piedraTexture));
                }
            }
        }

        personaje = new Personaje("Frames.png", 16, 16);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.WHITE);
        delta = Gdx.graphics.getDeltaTime();

        personaje.reiniciarSaltos(delta, bloques);
        personaje.update(delta);
        logic();

        spriteBatch.begin();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        for (Bloque bloque : bloques) {
            spriteBatch.draw(bloque.getTextura(), bloque.x, bloque.y, bloque.width, bloque.height);
        }
        personaje.dibujar(spriteBatch);
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
        System.out.println(mouse_position.x + ", " + mouse_position.y);

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
