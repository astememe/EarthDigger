package io.github.EarthDigger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class EarthDigger extends ApplicationAdapter {
    Array<Bloque> bloques;
    Mapa mapa;
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

        personaje = new Personaje("Frames.png", 14, 16);
    }

    @Override
    public void render() {
        delta = Gdx.graphics.getDeltaTime();

        // Manejo controles (puedes pasarlo al personaje o manejar aquí)
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
        personaje.update(delta);

        spriteBatch.begin();
        personaje.dibujar(spriteBatch);
        spriteBatch.end();

        ScreenUtils.clear(Color.WHITE);
        viewport.apply();

        camera.update();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        camera.position.x = personaje.getX() + personaje.getAncho() / 2f;
        camera.position.y = personaje.getY() + personaje.getAlto() / 2f;


        for (Bloque bloque : bloques) {
            spriteBatch.draw(bloque.getTextura(), bloque.x, bloque.y, bloque.width, bloque.height);
        }

        personaje.dibujar(spriteBatch);

        spriteBatch.end();
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
