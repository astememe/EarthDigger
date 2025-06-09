package io.github.EarthDigger;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class JuegoScreen implements Screen {
    private EarthDigger game;
    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private Personaje personaje;
    private Mapa mapa;
    private ArrayList<Bloque> bloques;
    private int[][] mapa_forma;
    private int[] true_mouse_position = new int[2];
    private int screenSizeX = 320;
    private int screenSizeY = 48;
    private float delta;
    private int mapWidth;

    public JuegoScreen(EarthDigger game) {
        this.game = game;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ExtendViewport(screenSizeX, screenSizeY, camera);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        mapa = new Mapa();
        mapa_forma = mapa.getForma();
        mapWidth = mapa_forma[0].length;
        bloques = new ArrayList<>();

        spriteBatch = new SpriteBatch();

        Assets.load();
        mapa.rellenarMapa(bloques);

        personaje = new Personaje("Frames.png", 16, 16);
    }

    @Override
    public void render(float delta) {
        this.delta = delta;
        ScreenUtils.clear(Color.WHITE);
        logic();

        stage.act(delta);
        stage.draw();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (Bloque bloque : bloques) {
            spriteBatch.draw(bloque.getTextura(), bloque.x, bloque.y, bloque.width, bloque.height);
        }
        personaje.dibujar(spriteBatch);
        spriteBatch.end();

        camera.update();
    }

    public void logic() {
        mapa.rellenarMapa(bloques);
        personaje.reiniciarSaltos(delta, bloques);
        personaje.update(delta);

        camera.position.x = personaje.getX() + personaje.getAncho() / 2f;
        camera.position.y = personaje.getY() + personaje.getAlto() / 2f;

        if (camera.position.x < viewport.getWorldWidth() / 2f) {
            camera.position.x = viewport.getWorldWidth() / 2f;
        } else if (camera.position.x > mapWidth * 16 - viewport.getWorldWidth() / 2f) {
            camera.position.x = mapWidth * 16 - viewport.getWorldWidth() / 2f;
        }

        if (camera.position.y < viewport.getWorldHeight() / 2f) {
            camera.position.y = viewport.getWorldHeight() / 2f - 16 * (mapa.getForma().length - 1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                personaje.moverIzquierda(delta*2);
            }
            personaje.moverIzquierda(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)){
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                personaje.moverDerecha(delta*2);
            }
            personaje.moverDerecha(delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) personaje.saltar();
    }

    @Override public void resize(int width, int height) { viewport.update(width, height); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        personaje.dispose();
        spriteBatch.dispose();
        Assets.dispose();
    }
}
