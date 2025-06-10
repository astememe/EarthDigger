package io.github.EarthDigger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Personaje {
    protected Rectangle personajeHitbox;
    protected Sprite sprite;

    protected int cantSaltos = 0;
    protected int vida = 5;
    private int bloqueEquipado = 1;


    protected float velocidadY = 0;
    protected float gravedadNormal = -100;
    protected float gravedadCaida = -300;
    protected float stateTime;
    protected float posX, posY;
    protected float ancho, alto;

    protected boolean saltando = false;
    protected boolean corriendo = false;
    protected boolean mirandoDerecha = true;
    protected boolean moviendose = false;
    protected boolean muerto = false;

    private float tiempoDesdeUltimoGolpe = 0f;
    private final float COOLDOWN_GOLPE = 2f;
    protected Animation<TextureRegion> caminarDerechaAnim;
    protected Animation<TextureRegion> caminarIzquierdaAnim;
    protected Animation<TextureRegion> quietoAnim;
    protected TextureRegion frameActual;

    public Personaje(String rutaSpriteSheet, float ancho, float alto) {
        this.ancho = ancho;
        this.alto = alto;
        this.posX = 0;
        this.posY = 0;

        Texture spriteSheet = new Texture(Gdx.files.internal(rutaSpriteSheet));
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, 16, 16); // 6x6 pero usamos 3 filas x 2 columnas

        // Animación caminar derecha (fila 0)
        TextureRegion[] caminarDerechaFrames = new TextureRegion[2];
        caminarDerechaFrames[0] = tmp[0][0];
        caminarDerechaFrames[1] = tmp[0][1];
        caminarDerechaAnim = new Animation<>(0.35f, caminarDerechaFrames);

        // Animación caminar izquierda (fila 1)
        TextureRegion[] caminarIzquierdaFrames = new TextureRegion[2];
        caminarIzquierdaFrames[0] = tmp[1][0];
        caminarIzquierdaFrames[1] = tmp[1][1];
        caminarIzquierdaAnim = new Animation<>(0.35f, caminarIzquierdaFrames);

        // Animación quieto (fila 2)
        TextureRegion[] quietoFrames = new TextureRegion[2];
        quietoFrames[0] = tmp[2][0];
        quietoFrames[1] = tmp[2][1];
        quietoAnim = new Animation<>(1f, quietoFrames); // más lenta si quieres que parpadee o respire

        frameActual = quietoFrames[0];
        stateTime = 0f;

        frameActual = quietoFrames[0];
        stateTime = 0f;

        personajeHitbox = new Rectangle(posX, posY, ancho-2, alto);
    }

    public void moverIzquierda(float delta) {
        posX -= delta * 32;
        moviendose = true;
        mirandoDerecha = false;
    }

    public void moverDerecha(float delta) {
        posX += delta * 32;
        moviendose = true;
        mirandoDerecha = true;
    }

    public void saltar() {
        if (cantSaltos < 2) {
            velocidadY = 70;
            saltando = true;
            cantSaltos++;
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
        moviendose = false; // reset para el siguiente frame


    }

    public void dibujar(SpriteBatch batch) {
        batch.draw(frameActual, posX, posY, ancho, alto);
    }

    public Rectangle getHitbox() {
        return personajeHitbox;
    }

    public float getX() {
        return posX;
    }

    public float getY() {
        return posY;
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

    public int getBloqueEquipado() {
        return bloqueEquipado;
    }

    public void setBloqueEquipado(int bloqueEquipado) {
        this.bloqueEquipado = bloqueEquipado;
    }
    public void setPosicion(float x, float y) {
        this.posX = x;
        this.posY = y;
        personajeHitbox.setPosition(x, y);
    }

    public void recibirGolpe() {
        if (!muerto && tiempoDesdeUltimoGolpe >= COOLDOWN_GOLPE) {
            vida--;
            System.out.println("El personaje recibio un golpe!\nVida restante: " + vida);
            tiempoDesdeUltimoGolpe = 0f;
            if (vida <= 0) {
                muerto = true;
                System.out.println("Has muerto...");
            }
        }
    }
}
