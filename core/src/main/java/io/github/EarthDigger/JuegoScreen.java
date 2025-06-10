package io.github.EarthDigger;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
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
    private Vector3 mouse_position = new Vector3(0,0,0);
    private Vector3 mouse_snapshot = new Vector3(0,0,0);
    private int[] true_mouse_position = new int[2];
    private int screenSizeX = 320;
    private int screenSizeY = 48;
    private float delta;
    private int mapWidth;
    private float tiempoDesdeUltimoSpawn = 0f;
    private float intervaloSpawn = 10f; // Tiempo de aparición de los enemigos.
    private ArrayList<Enemy> enemigos = new ArrayList<>();
    private float velocidadEnemigos = 20f; // Belocidad de los enemigos.

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
        for (int fila = mapa_forma.length - 1; fila >= 0; fila--) {
            for (int columna = 0; columna < mapa_forma[0].length; columna++) {
                if (mapa_forma[fila][columna] != 0) {
                    float x = columna * 16;
                    float y = -(fila + 1) * 16;
                    personaje.setPosicion(x, y);
                    break;
                }
            }
        }
    }

    @Override
    public void render(float delta) {
        this.delta = delta;
        ScreenUtils.clear(Color.WHITE);
        logic();

        for (Enemy enemigo : enemigos) {
            enemigo.reiniciarSaltos(delta, bloques);
            enemigo.seguirAlPersonaje(personaje, delta, velocidadEnemigos);
            if (enemigo.getHitbox().overlaps(personaje.getHitbox())) {
                personaje.recibirGolpe();
            }
        }

        stage.act(delta);
        stage.draw();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        for (Bloque bloque : bloques) {
            spriteBatch.draw(bloque.getTextura(), bloque.x, bloque.y, bloque.width, bloque.height);
        }

        personaje.dibujar(spriteBatch);

        for (Enemy enemigo : enemigos) {
            enemigo.dibujar(spriteBatch);
        }

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

        tiempoDesdeUltimoSpawn += delta;
        if (tiempoDesdeUltimoSpawn >= intervaloSpawn) {
            spawnEnemy();
            tiempoDesdeUltimoSpawn = 0f;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                personaje.moverIzquierda(delta * 2);
            } else {
                personaje.moverIzquierda(delta);
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                personaje.moverDerecha(delta * 2);
            } else {
                personaje.moverDerecha(delta);
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            personaje.saltar();
        }

        mouse_position.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mouse_position);

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            mouse_snapshot = mouse_position;
            true_mouse_position[0] = (int)mouse_snapshot.x / 16;
            true_mouse_position[1] = (mouse_snapshot.y > 0)
                ? (int)mouse_snapshot.y / 16
                : (int)mouse_snapshot.y / 16 - 1;
            System.out.println(true_mouse_position[0] + ", " + true_mouse_position[1]);
            if (-true_mouse_position[1] != mapa_forma.length - 1) {
                mapa_forma[-true_mouse_position[1]][true_mouse_position[0]] = 0;
                mapa.setForma(mapa_forma);
            }
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            mouse_snapshot = mouse_position;
            true_mouse_position[0] = (int)mouse_snapshot.x / 16;
            true_mouse_position[1] = (mouse_snapshot.y > 0)
                ? (int)mouse_snapshot.y / 16
                : (int)mouse_snapshot.y / 16 - 1;
            System.out.println(true_mouse_position[0] + ", " + true_mouse_position[1]);
            if (-true_mouse_position[1] != mapa_forma.length - 1) {
                mapa_forma[-true_mouse_position[1]][true_mouse_position[0]] = 1;
                mapa.setForma(mapa_forma);
            }
        }
    }

    private void spawnEnemy() {
        if (enemigos.size() >= 5) return;

        Enemy nuevoEnemigo = new Enemy("Frames.png", 16, 16);
        int columna = (int)(Math.random() * mapa_forma[0].length);

        for (int fila = mapa_forma.length - 1; fila >= 0; fila--) {
            if (mapa_forma[fila][columna] != 0) {
                float x = columna * 16;
                float y = -fila * 16 + 16;
                nuevoEnemigo.setPosicion(x, y);
                enemigos.add(nuevoEnemigo);
            }
        }
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
