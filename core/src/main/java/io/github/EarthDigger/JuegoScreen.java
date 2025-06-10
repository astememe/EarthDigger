package io.github.EarthDigger;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class JuegoScreen implements Screen {
    //Fondo
    private boolean esDia = false;
    private float tiempoTranscurrido = 0f;
    private final float intervaloCambio = 60f; // cambiar cada 10 segundos, por ejemplo
    private Texture fondoDia;
    private Texture fondoNoche;
    private Texture fondoActual;


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
    private int screenSizeX = 16*30;
    private int screenSizeY = 48;
    private float delta;
    private int mapWidth;
    private float tiempoDesdeUltimoSpawn = 0f;
    private float intervaloSpawn = 10f; // Tiempo de aparición de los enemigos.
    private ArrayList<Enemy> enemigos = new ArrayList<>();
    private float velocidadEnemigos = 20f; // Velocidad de los enemigos.

    public JuegoScreen(EarthDigger game) {
        this.game = game;
    }

    @Override
    public void show() {
        fondoDia = new Texture("FONDOS\\dia.png");
        fondoNoche = new Texture("FONDOS\\noche.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenSizeX, screenSizeY);
        viewport = new ExtendViewport(screenSizeX, screenSizeY, camera);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        mapa = new Mapa();
        mapa_forma = mapa.getForma();
        mapWidth = mapa_forma[0].length * 16;
        bloques = new ArrayList<>();

        spriteBatch = new SpriteBatch();

        Assets.load();
        mapa.rellenarMapa(bloques);

        personaje = new Personaje("PERSONAJEACTUALIZADO\\Frames.png", 16, 16);

        int columnaInicial = 1;
        for (int fila = mapa_forma.length - 1; fila >= 0; fila--) {
            if (mapa_forma[fila][columnaInicial] != 0) {
                float x = columnaInicial * 16;
                float y = -fila * 16 + 16;
                personaje.setPosicion(x, y);
                break;
            }
        }
    }

    @Override
    public void render(float delta) {
        this.delta = delta;
        ScreenUtils.clear(Color.BLACK);

        logic();

        camera.update();

        stage.act(delta);
        stage.draw();

        //CAMBIO DE FONDO
        if (esDia) {
            fondoActual = fondoDia;
        } else {
            fondoActual = fondoNoche;
        }
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(fondoActual, 0, -mapa_forma.length*16, 180*16, 20*16);
        spriteBatch.end();


        //CARGAR SPRITES
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        for (Bloque bloque : bloques) {
            spriteBatch.draw(bloque.getTextura(), bloque.x, bloque.y, bloque.width, bloque.height);
        }

        // DIBUJAR ENEMIGOS - MODIFICADO: ahora sí se dibujan
        for (Enemy enemigo : enemigos) {
            enemigo.dibujar(spriteBatch);
        }
        // ACTUALIZAR ENEMIGOS Y COLISIONES
        for (Enemy enemigo : enemigos) {
            enemigo.reiniciarSaltos(delta, bloques);
            enemigo.seguirAlPersonaje(personaje, delta, velocidadEnemigos);
            enemigo.update(delta); // MODIFICADO: actualizar animación y frameActual
            if (enemigo.getHitbox().overlaps(personaje.getHitbox())) {
                personaje.recibirGolpe();
            }
        }

        // DIBUJAR PERSONAJE (que estaba después de los enemigos)
        personaje.dibujar(spriteBatch);

        spriteBatch.end();

        stage.act(delta);
        stage.draw();
    }


    public void logic() {
        mapa.rellenarMapa(bloques);
        personaje.reiniciarSaltos(delta, bloques);
        personaje.update(delta);


        //LA CAMARA SIGUE AL PERSONAJE UWU
        camera.position.x = personaje.getX() + personaje.getAncho() / 2f;
        camera.position.y = personaje.getY() + personaje.getAlto() / 2f;


        //CAMBIO DE FONDO
        tiempoTranscurrido += delta;
        if (tiempoTranscurrido >= intervaloCambio) {
            esDia = !esDia; // cambia entre día y noche
            tiempoTranscurrido = 0f;
        }


        //BLOQUEAR LA CAMARA AL LLEGAR AL BORDE DE LA PANTALLA
        if (camera.position.x < viewport.getWorldWidth() / 2f) {
            camera.position.x = viewport.getWorldWidth() / 2f;
        } else if (camera.position.x > mapWidth - viewport.getWorldWidth() / 2f) {
            camera.position.x = mapWidth - viewport.getWorldWidth() / 2f;
        }

        if (camera.position.y < viewport.getWorldHeight() / 2f) {
            camera.position.y = viewport.getWorldHeight() / 2f - 16 * (mapa.getForma().length - 1);
        }

        if (!esDia) {
            tiempoDesdeUltimoSpawn += delta;
            if (tiempoDesdeUltimoSpawn >= intervaloSpawn) {
                spawnEnemy();
                tiempoDesdeUltimoSpawn = 0f;
            }
        } else {
            for (int i = 0; i < enemigos.size(); i++) {
                enemigos.remove(i); //Para que desaparezcan los enemigos cuando sea de dia.
            }
        }

        //CONTROLES
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                personaje.moverIzquierda((float) (delta*1.2));
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
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) personaje.setBloqueEquipado(1);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) personaje.setBloqueEquipado(2);
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) personaje.setBloqueEquipado(3);


        //POSICION RATON, ROMPER BLOQUES, PONER BLOQUES
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
            if (-true_mouse_position[1] != mapa_forma.length - 1 ) {
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
            if (-true_mouse_position[1] != mapa_forma.length - 1 && mapa_forma[-true_mouse_position[1]][true_mouse_position[0]] == 0) {
                mapa_forma[-true_mouse_position[1]][true_mouse_position[0]] = personaje.getBloqueEquipado();
                mapa.setForma(mapa_forma);
            }
        }
    }

    private void spawnEnemy() {
        if (enemigos.size() >= 5) return;

        Enemy nuevoEnemigo = new Enemy("PERSONAJEACTUALIZADO\\Frames.png", 16, 16);
        int columna = (int)(Math.random() * mapa_forma[0].length);

        for (int fila = mapa_forma.length - 1; fila >= 0; fila--) {
            if (mapa_forma[fila][columna] != 0) {
                float x = columna * 16;
                float y = -fila * 16 + 16;
                nuevoEnemigo.setPosicion(x, y);
                enemigos.add(nuevoEnemigo);
                break;
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
