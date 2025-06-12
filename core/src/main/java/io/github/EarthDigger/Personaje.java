package io.github.EarthDigger;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

public class Personaje {
    private float delta = Gdx.graphics.getDeltaTime();
    private Rectangle personajeHitbox;
    private Sprite sprite;

    private int cantSaltos = 0;
    private int vidaTotal = 5;
    private ArrayList<Texture> vida = new ArrayList<>();
    private int bloqueEquipadoNum = 1;
    private ArrayList<Texture> inventario = new ArrayList<>();
    private int monedasCant = 0;

    private float velocidadY = 0;
    private float velocidadX = 32;
    private float gravedadNormal = -100;
    private float gravedadCaida = -300;
    private float stateTime;
    private float posX, posY;
    private float ancho, alto;
    private float tiempoDesdeUltimoGolpe = 0f;
    private final float COOLDOWN_GOLPE = 1.5f;

    private boolean saltando = false;
    private boolean corriendo = false;
    private boolean mirandoDerecha = true;
    private boolean moviendose = false;
    private boolean muerto = false;

    private Random random = new Random();
    private boolean cavado;

    private Animation<TextureRegion> caminarDerechaAnim;
    private Animation<TextureRegion> caminarIzquierdaAnim;
    private Animation<TextureRegion> quietoAnim;
    private TextureRegion frameActual;

    public Personaje(String rutaSpriteSheet, float ancho, float alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.posX = 0;
        this.posY = 0;

        this.vida = new ArrayList<>();
        for (int i = 0; i < vidaTotal; i++) {
            vida.add(new Texture(Gdx.files.internal("PERSONAJE\\corazon.png")));
        }

        this.inventario = new ArrayList<>();
        inventario.add(Assets.cespedTexture);
        inventario.add(Assets.tierraTexture);
        inventario.add(Assets.piedraTexture);

        Texture spriteSheet = new Texture(Gdx.files.internal(rutaSpriteSheet));
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, 16, 16);

        // Animación caminar derecha
        TextureRegion[] caminarDerechaFrames = new TextureRegion[2];
        caminarDerechaFrames[0] = tmp[0][0];
        caminarDerechaFrames[1] = tmp[0][1];
        caminarDerechaAnim = new Animation<>(0.5f, caminarDerechaFrames);

        // Animación caminar izquierda
        TextureRegion[] caminarIzquierdaFrames = new TextureRegion[2];
        caminarIzquierdaFrames[0] = tmp[1][0];
        caminarIzquierdaFrames[1] = tmp[1][1];
        caminarIzquierdaAnim = new Animation<>(0.5f, caminarIzquierdaFrames);

        // Animación quieto
        TextureRegion[] quietoFrames = new TextureRegion[2];
        quietoFrames[0] = tmp[2][0];
        quietoFrames[1] = tmp[2][1];
        quietoAnim = new Animation<>(1f, quietoFrames);

        frameActual = quietoFrames[0];
        stateTime = 0f;

        personajeHitbox = new Rectangle(posX, posY, ancho-2, alto);
    }

    public void moverIzquierda(float delta) {
        posX -= delta * velocidadX;
        moviendose = true;
        mirandoDerecha = false;
    }

    public void moverDerecha(float delta) {
        posX += delta * velocidadX;
        moviendose = true;
        mirandoDerecha = true;
    }

    public void saltar() {
        if (cantSaltos < 2) {
            velocidadY = 70;
            saltando = true;
            cantSaltos++;
            Audios.getInstance().playSonidoSalto();
        }
    }

    public void reiniciarSaltos(float delta, ArrayList<Bloque> bloques) {
        if (velocidadY < 0) {
            velocidadY += gravedadCaida * delta;
        } else {
            velocidadY += gravedadNormal * delta;
        }

        float nuevaY = posY + velocidadY * delta;
        Rectangle nuevaHitbox = new Rectangle(posX + 2, nuevaY, ancho - 4, alto);

        boolean colisionVertical = false;

        for (Bloque bloque : bloques) {
            if (nuevaHitbox.overlaps(bloque)) {
                if (velocidadY < 0) {
                    posY = bloque.y + bloque.height;
                    velocidadY = 0;
                    cantSaltos = 0;
                    saltando = false;
                } else if (velocidadY > 0) {
                    posY = bloque.y - alto;
                    velocidadY = 0;
                }
                colisionVertical = true;
            }
        }

        if (!colisionVertical) {
            posY = nuevaY;
        }

        personajeHitbox.setPosition(posX, posY);
    }

    public void comprarVelocidad() {
        this.monedasCant = monedasCant - 10;
        this.velocidadX +=8;
    }

    public void update(float delta) {
        stateTime += delta;

        if (moviendose) {
            if (mirandoDerecha) {
                frameActual = caminarDerechaAnim.getKeyFrame(stateTime, true);
            } else {
                frameActual = caminarIzquierdaAnim.getKeyFrame(stateTime, true);
            }
        } else {
            frameActual = quietoAnim.getKeyFrame(stateTime, true);
        }

        personajeHitbox.setPosition(posX, posY);
        moviendose = false;

        //TIEMPO PARA COLISIONES
        tiempoDesdeUltimoGolpe+= (float) (0.5*delta);

    }

    public void dibujar(SpriteBatch batch) {
        batch.draw(frameActual, posX, posY, ancho, alto);
    }

    public Rectangle getHitbox() {
        return personajeHitbox;
    }

    public TextureRegion getFrameActual() {
        return frameActual;
    }

    public void setFrameActual(TextureRegion frameActual) {
        this.frameActual = frameActual;
    }

    public float getX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getY() {
        return posY;
    }


    public Animation<TextureRegion> getCaminarDerechaAnim() {
        return caminarDerechaAnim;
    }

    public void setCaminarDerechaAnim(Animation<TextureRegion> caminarDerechaAnim) {
        this.caminarDerechaAnim = caminarDerechaAnim;
    }

    public Animation<TextureRegion> getCaminarIzquierdaAnim() {
        return caminarIzquierdaAnim;
    }

    public void setCaminarIzquierdaAnim(Animation<TextureRegion> caminarIzquierdaAnim) {
        this.caminarIzquierdaAnim = caminarIzquierdaAnim;
    }

    public Animation<TextureRegion> getQuietoAnim() {
        return quietoAnim;
    }

    public void setQuietoAnim(Animation<TextureRegion> quietoAnim) {
        this.quietoAnim = quietoAnim;
    }


    public void dispose() {

    }
    public Sprite getSprite() {
        return sprite;
    }

    public Vector2 getPosicion() {
        return new Vector2(posX, posY);
    }

    public float getAncho() { return ancho; }
    public float getAlto() { return alto; }

    public int getBloqueEquipadoNum() {
        return bloqueEquipadoNum;
    }

    public void setBloqueEquipadoNum(int bloqueEquipado) {
        this.bloqueEquipadoNum = bloqueEquipado;
    }
    public void setPosicion(float x, float y) {
        this.posX = x;
        this.posY = y;
        personajeHitbox.setPosition(x, y);
    }



    public void recibirGolpe() {
        if (!muerto && tiempoDesdeUltimoGolpe >= COOLDOWN_GOLPE) {
            vida.removeFirst();
            System.out.println("El personaje recibio un golpe!\nVida restante: " + vida.size());
            tiempoDesdeUltimoGolpe = 0f;
            if (getVida().size() >= 1) {
                Audios.getInstance().playSonidoGolpe();
            }
            if (vida.isEmpty()) {
                muerto = true;
                System.out.println("Has muerto...");
                Audios.getInstance().playSonidoMuerte();
                Audios.getInstance().pausarMusica();
            }
        }
    }

    public ArrayList<Texture> getVida() {
        return vida;
    }

    public ArrayList<Texture> getInventario() {
        return inventario;
    }

    public void conseguirMoneda() {
        int check = random.nextInt(1, 10);
        if (cavado && check == 1) {
            monedasCant++;
        }
    }

    public int getMonedasCant() {
        return monedasCant;
    }

    public void setCavado(boolean cavado) {
        this.cavado = cavado;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
