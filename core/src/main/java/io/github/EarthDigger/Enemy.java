package io.github.EarthDigger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemy extends Personaje {
    private boolean mirandoDerecha = true;
    private boolean moviendose = false;
    private float stateTime = 0f;

    public Enemy(String rutaSpriteSheet, float ancho, float alto) {
        super(rutaSpriteSheet, ancho, alto);
    }

    public void seguirAlPersonaje(Personaje personaje, float delta, float velocidad) {
        float objetivoX = personaje.getX();
        float diferencia = objetivoX - this.posX;

        if (Math.abs(diferencia) > 1) {
            if (diferencia > 0) {
                this.posX += velocidad * delta;
                mirandoDerecha = true;
            } else {
                this.posX -= velocidad * delta;
                mirandoDerecha = false;
            }
            moviendose = true;
        } else {
            moviendose = false;
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
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
        getHitbox().setPosition(getX(), getY());
        moviendose = false;
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        batch.draw(frameActual, getX(), getY(), getAncho(), getAlto());
    }
}
