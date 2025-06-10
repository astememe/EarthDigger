package io.github.EarthDigger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemy2 extends Personaje {
    private boolean mirandoDerecha = true;
    private boolean moviendose = false;
    private float stateTime = 0f;

    public Enemy2(String rutaSpriteSheet, float ancho, float alto) {
        super(rutaSpriteSheet, ancho, alto);
    }

    public void moverse(){

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
