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
    float delta;
    int screenSizeX = 320;
    int screenSizeY = 48;
    Viewport viewport;
    OrthographicCamera camera;
    Personaje personaje;

    SpriteBatch spriteBatch;

    @Override
    public void create() {
        bloques = new Array<>();
        personaje = new Personaje("Frames.png", 16, 16);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ExtendViewport(screenSizeX, screenSizeY, camera);

        spriteBatch = new SpriteBatch();

        Assets.load();

        int blockSize = 16;
        int mapWidth = 200;

        // Generar bloques con clase Bloque y texturas de Assets
        for (int x = 0; x < mapWidth; x++) {
            bloques.add(new Bloque(x * blockSize, 5 * blockSize, blockSize, blockSize, Assets.cespedTexture));

            for (int y = 1; y < 5; y++) {
                bloques.add(new Bloque(x * blockSize, y * blockSize, blockSize, blockSize, Assets.tierraTexture));
            }

            bloques.add(new Bloque(x * blockSize, 0, blockSize, blockSize, Assets.piedraTexture));
        }

        personaje = new Personaje("Frames.png", 16, 16);
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
