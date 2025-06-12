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
import java.util.Random;

public class Juego implements Screen {
    //EasterEgg
    private Texture pescaoTexture = new Texture("pescao.png");
    private Texture pinyaTexture = new Texture("images.jpg");
    private boolean easterEgg = false;

    //Game
    private EarthDigger game;
    private float delta;

    //Fondo
    private boolean esDia = false;
    private float tiempoTranscurrido = 0f;
    private final float intervaloCambio = 60f;
    private Texture fondoDia;
    private Texture fondoNoche;
    private Texture fondoActual;

    //Sprite
    private SpriteBatch spriteBatch;

    //Personaje
    private Personaje personaje;

    //Enemigos
    private ArrayList<Enemy> enemigos = new ArrayList<>();
    private ArrayList<Enemy> enemigosBajoTierra = new ArrayList<>();
    private float tiempoDesdeUltimoSpawn = 0f;
    private float tiempoDesdeUltimoSpawnBajoTierra = 0f;
    private float intervaloSpawn = 5;
    private float velocidadEnemigos = 20f;

    //Enemigos 2
    private ArrayList<Enemy2> enemigos2 = new ArrayList<>();
    private float intervaloSpawn2 = 15f;
    private float tiempoDesdeUltimoSpawn2 = 0f;


    //Mapa
    private Mapa mapa;
    private int mapWidth;
    private Stage stage;
    private int[][] mapa_forma;

    //Pantalla
    private int screenSizeX = 16*30;
    private Viewport viewport;
    private OrthographicCamera camera;
    private int screenSizeY = 48;

    //Mouse
    private Vector3 mouse_position = new Vector3(0,0,0);
    private Vector3 mouse_snapshot = new Vector3(0,0,0);
    private int[] true_mouse_position = new int[2];

    //Bloques
    private ArrayList<Bloque> bloques;

    public Juego(EarthDigger game) {
        this.game = game;
    }

    @Override
    public void show() {
        Audios.getInstance().playMusicaFondo();
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

        personaje = new Personaje("PERSONAJE\\Frames.png", 16, 16);

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
        Audios.getInstance().reanudarMusica();
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
        for (int i = 0; i < personaje.getVida().size(); i++) {
            spriteBatch.draw(personaje.getVida().get(i), camera.position.x + viewport.getWorldWidth() / 2f - 20 - i*16, camera.position.y + viewport.getWorldHeight() / 2f - 20, 16, 16);
        }

        spriteBatch.draw(personaje.getInventario().get(personaje.getBloqueEquipadoNum() - 1), camera.position.x + viewport.getWorldWidth() / 2f - 20, camera.position.y + viewport.getWorldHeight() / 2f - 20 - 16, 16, 16);

        for (Bloque bloque : bloques) {
            spriteBatch.draw(bloque.getTextura(), bloque.x, bloque.y, bloque.width, bloque.height);
        }

        if (!easterEgg) {
            for (Enemy enemigo : enemigos) {
                enemigo.dibujar(spriteBatch);
            }
            for (Enemy2 enemy2:enemigos2) {
                enemy2.dibujar(spriteBatch);
            }
            for (Enemy enemigoBajoTierra : enemigosBajoTierra) {
                enemigoBajoTierra.dibujar(spriteBatch);
            }
            personaje.dibujar(spriteBatch);
        } else {
            for (Enemy enemigo : enemigos) {
                spriteBatch.draw(pescaoTexture, enemigo.getX(), enemigo.getY(), 16,16);
            }
            for (Enemy2 enemy2:enemigos2) {
                enemy2.dibujar(spriteBatch);
            }
            for (Enemy enemigoBajoTierra : enemigosBajoTierra) {
                enemigoBajoTierra.dibujar(spriteBatch);
            }
            spriteBatch.draw(pinyaTexture, personaje.getX(), personaje.getY(), 16, 16);
        }

        String monedasText = "Monedas: "+ personaje.getMonedasCant();
        Assets.font.draw(spriteBatch, monedasText, camera.position.x - viewport.getWorldWidth() / 2f + 20, camera.position.y + viewport.getWorldHeight() / 2f - 20);
        String mejoraVelocidad = "Pulsa \"E\" para comprar mejora de velocidad";
        if (personaje.getMonedasCant() >= 10) {
            Assets.font.draw(spriteBatch, mejoraVelocidad, camera.position.x - viewport.getWorldWidth() / 2f + 20, camera.position.y + viewport.getWorldHeight() / 2f - 40);
        }
        spriteBatch.end();

        stage.act(delta);
        stage.draw();
    }

    public void logic() {
        Audios.getInstance().playMusicaFondo();
        mapa.rellenarMapa(bloques);
        personaje.reiniciarSaltos(delta, bloques);
        personaje.update(delta);

        //EASTER EGG
        pinya(personaje);

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

        // Control del tiempo de spawn para Enemy
        if (!esDia) {
            tiempoDesdeUltimoSpawn += delta;
            if (tiempoDesdeUltimoSpawn >= intervaloSpawn) {
                spawnEnemy();
                tiempoDesdeUltimoSpawn = 0f;
            }
        } else {
            enemigos.clear();
        }


        if (!esDia) {
            tiempoDesdeUltimoSpawnBajoTierra += delta;
            if (tiempoDesdeUltimoSpawnBajoTierra >= intervaloSpawn) {
                spawnEnemyBajoTierra();
                tiempoDesdeUltimoSpawnBajoTierra = 0f;
            }
        } else {
            enemigosBajoTierra.clear();
        }

        // Control del tiempo de spawn para Enemy2
        tiempoDesdeUltimoSpawn2 += delta;
        if (tiempoDesdeUltimoSpawn2 >= intervaloSpawn2) {
            spawnEnemy2();
            tiempoDesdeUltimoSpawn2 = 0f;
        }


        for (int i = enemigos2.size() - 1; i >= 0; i--) {
            Enemy2 e2 = enemigos2.get(i);
            e2.mover(enemigos2.get(i), delta, 80f);
            e2.update(delta);

            if (e2.getX() > mapWidth) {
                enemigos2.remove(i);
            }

            if (e2.getHitbox().overlaps(personaje.getHitbox())) {
                personaje.recibirGolpe();
            }
        }


        //CONTROLES
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                personaje.moverIzquierda((float) (delta*1.5));
            }
            personaje.moverIzquierda(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                personaje.moverDerecha((float) (delta*1.5));
            }
            personaje.moverDerecha(delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.W) || Gdx.input.isKeyJustPressed(Input.Keys.UP)) personaje.saltar();
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            personaje.setBloqueEquipadoNum(1);
            Audios.getInstance().sonidoCambioBloque();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            personaje.setBloqueEquipadoNum(2);
            Audios.getInstance().sonidoCambioBloque();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            personaje.setBloqueEquipadoNum(3);
            Audios.getInstance().sonidoCambioBloque();
        }

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
                Audios.getInstance().sonidoBloqueQuitar();
            }
            System.out.println(true_mouse_position[0] + ", " + true_mouse_position[1]);
            if (-true_mouse_position[1] != mapa_forma.length - 1 && mapa_forma[-true_mouse_position[1]][true_mouse_position[0]] != 0) {
                mapa_forma[-true_mouse_position[1]][true_mouse_position[0]] = 0;
                mapa.setForma(mapa_forma);
                //CONSEGUIR MONEDA 10%
                personaje.setCavado(true);
                personaje.conseguirMoneda();
                personaje.setCavado(false);
            }
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            mouse_snapshot = mouse_position;
            true_mouse_position[0] = (int)mouse_snapshot.x/16;
            if (mouse_snapshot.y > 0) {
                true_mouse_position[1] = (int)mouse_snapshot.y/16;
            } else {
                true_mouse_position[1] = (int)mouse_snapshot.y/16 - 1;
                Audios.getInstance().sonidoBloquePoner();
            }
            System.out.println(true_mouse_position[0] + ", " + true_mouse_position[1]);
            if (-true_mouse_position[1] != mapa_forma.length - 1 && mapa_forma[-true_mouse_position[1]][true_mouse_position[0]] == 0) {
                mapa_forma[-true_mouse_position[1]][true_mouse_position[0]] = personaje.getBloqueEquipadoNum();
                mapa.setForma(mapa_forma);
            }
        }

        // ACTUALIZAR ENEMIGOS Y COLISIONES
        for (Enemy enemigo : enemigos) {
            enemigo.reiniciarSaltos(delta, bloques);
            enemigo.seguirAlPersonaje(personaje, delta, velocidadEnemigos);
            enemigo.update(delta);

            if (enemigo.getHitbox().overlaps(personaje.getHitbox())) {
                personaje.recibirGolpe();
            }
        }

        for (Enemy enemigoBajoTierra : enemigosBajoTierra) {
            enemigoBajoTierra.reiniciarSaltos(delta, bloques);
            enemigoBajoTierra.seguirAlPersonaje(personaje, delta, velocidadEnemigos);
            enemigoBajoTierra.update(delta);

            // Verificar colisión dentro del bucle
            if (enemigoBajoTierra.getHitbox().overlaps(personaje.getHitbox())) {
                personaje.recibirGolpe();
            }
        }

        for (Enemy2 enemigo2 : enemigos2) {
            enemigo2.reiniciarSaltos(delta, bloques);
            enemigo2.mover(enemigo2, delta, 80f);
            enemigo2.update(delta);

            if (enemigo2.getHitbox().overlaps(personaje.getHitbox())) {
                personaje.recibirGolpe();
            }
        }

        if (personaje.getVida().isEmpty()) {
            game.setScreen(new Muerte(game));
        }

        //MEJORA DE VEVLOCIDAD
        if (personaje.getMonedasCant() >=10) {
            if (Gdx.input.isKeyPressed(Input.Keys.E)) {
                personaje.comprarVelocidad();
            }
        }

        if ((int)personaje.getY()/16 < -16) {

        }
    }

    private void spawnEnemyBajoTierra() {
        if (enemigosBajoTierra.size() >= 2) return;

        int capaPersonaje = -(int)personaje.getY()/16;
        int posXPersonaje = (int)personaje.getX()/16;
        if (capaPersonaje != 16) return;

        Enemy nuevoEnemigoBajoTierra = new Enemy("ENEMIGOS\\SLIMETEXTURES.png", 16, 16);

        int distanciaDerecha = 0;
        for (int dx = 1; dx <= 10 && (posXPersonaje + dx < mapa_forma[0].length && mapa_forma[capaPersonaje][posXPersonaje + dx] == 0); dx++) {
            distanciaDerecha = dx;
        }

        int distanciaIzquierda = 0;
        for (int dx = 1; dx <= 10 && posXPersonaje - dx >= 0 && mapa_forma[capaPersonaje][posXPersonaje - dx] == 0; dx++) {
            distanciaIzquierda = dx;
        }

        if (distanciaDerecha > distanciaIzquierda && distanciaDerecha >= 4) {
            nuevoEnemigoBajoTierra.setPosicion((posXPersonaje + distanciaDerecha) * 16, personaje.getY());
            enemigosBajoTierra.add(nuevoEnemigoBajoTierra);
        }
        else if (distanciaIzquierda >= 4) {
            nuevoEnemigoBajoTierra.setPosicion((posXPersonaje - distanciaIzquierda) * 16, personaje.getY());
            enemigosBajoTierra.add(nuevoEnemigoBajoTierra);
        }
    }

    private void spawnEnemy() {
        if (enemigos.size() >= 5) return;

        Enemy nuevoEnemigo = new Enemy("ENEMIGOS\\SLIMETEXTURES.png", 16, 16);
        float spawnX;
        float spawnY = 0;

        boolean spawnLeft = Math.random() < 0.5;

        int columna;
        if (spawnLeft) {
            columna = (int) ((camera.position.x - viewport.getWorldWidth() / 2f - 32) / 16);
            if (columna < 0) columna = 0;
        } else {
            columna = (int) ((camera.position.x + viewport.getWorldWidth() / 2f + 32) / 16);
            if (columna >= mapa_forma[0].length) columna = mapa_forma[0].length - 1;
        }

        for (int fila = mapa_forma.length - 1; fila >= 0 && mapa_forma[fila][columna] != 0; fila--) {
            if (mapa_forma[fila][columna] != 0) {
                spawnX = columna * 16;
                spawnY = -fila * 16 + 16;
                nuevoEnemigo.setPosicion(spawnX, spawnY);
                break;
            }
        }
        enemigos.add(nuevoEnemigo);
    }

    private void spawnEnemy2() {
        if (enemigos2.size() >= 3) return;
        Enemy2 nuevoEnemigo2 = new Enemy2("ENEMIGOS\\CICLOPE.png", 16, 32);

        float spawnX;
        float spawnY;
        int columna;
        columna = (int) ((camera.position.x - viewport.getWorldWidth() / 2f - 32) / 16);
        if (columna < 0) columna = 0;

        for (int fila = mapa_forma.length - 1; fila >= 0 && mapa_forma[fila][columna] != 0; fila--) {
            if (mapa_forma[fila][columna] != 0) {
                spawnX = columna * 16;
                spawnY = -fila * 16 + 16;
                nuevoEnemigo2.setPosicion(spawnX, spawnY);
                break;
            }
        }
        enemigos2.add(nuevoEnemigo2);
    }

    public void pinya(Personaje personaje) {
        if (personaje.getVida().size() == 5 && personaje.getMonedasCant() == 10 && Gdx.input.isKeyPressed(Input.Keys.P)){
            if (Gdx.input.isKeyPressed(Input.Keys.I)) {
                if (Gdx.input.isKeyPressed(Input.Keys.N)) {
                    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                        easterEgg = true;
                    }
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        spriteBatch.dispose();
        personaje.dispose();
        Assets.dispose();
        fondoDia.dispose();
        fondoNoche.dispose();
        pescaoTexture.dispose();
        pinyaTexture.dispose();
    }
}
