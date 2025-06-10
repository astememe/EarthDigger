package io.github.EarthDigger;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Enemy extends Personaje {
    private boolean mirandoDerecha = true;
    private boolean moviendose = false;
    private float stateTime = 0f;
    Personaje personaje;

    public Enemy(String rutaSpriteSheet, float ancho, float alto) {
        super(rutaSpriteSheet, ancho, alto);
    }

    public void seguirAlPersonaje(Personaje personaje, float delta, float velocidad) {
        float objetivoX = personaje.getX();
        float diferencia = objetivoX - this.getX();

        if (Math.abs(diferencia) > 1) {
            if (diferencia > 0) {
                setPosX(this.getX() + velocidad * delta);
                mirandoDerecha = true;
            } else {
                setPosX(this.getX() - velocidad * delta);
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
