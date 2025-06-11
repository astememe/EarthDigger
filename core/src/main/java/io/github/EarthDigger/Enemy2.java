package io.github.EarthDigger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemy2 extends Personaje {
    private boolean mirandoDerecha = true;
    private boolean moviendose = false;
    private float stateTime = 0f;

    public Enemy2(String rutaSpriteSheet, float ancho, float alto) {
        super(rutaSpriteSheet, ancho, alto);
    }

    public void mover(Enemy2 enemy2, float delta, float velocidad) {
        this.setPosX(getX() + delta * velocidad);
        this.mirandoDerecha = true;
        this.moviendose = true;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        stateTime += delta;
        if (moviendose) {
            if (mirandoDerecha) {
                setFrameActual(getCaminarDerechaAnim().getKeyFrame(stateTime, true));
            } else {
                setFrameActual(getCaminarIzquierdaAnim().getKeyFrame(stateTime, true));
            }
        } else {
            setFrameActual(getQuietoAnim().getKeyFrame(stateTime, true));
        }
        getHitbox().setPosition(getX(), getY());
        moviendose = false;
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        batch.draw(getFrameActual(), getX(), getY(), getAncho(), getAlto());
    }
}
