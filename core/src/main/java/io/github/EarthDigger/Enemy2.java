package io.github.EarthDigger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy2 extends Personaje {
    private boolean mirandoDerecha = true;
    private boolean moviendose = false;
    private float stateTime = 0f;
    private Animation<TextureRegion> caminarDerechaAnim;
    private TextureRegion frameActual;

    public Enemy2(String rutaSpriteSheet, float ancho, float alto) {
        super(rutaSpriteSheet, ancho, alto);

        Texture spriteSheetCicplope = new Texture(Gdx.files.internal("subir a git\\Ciclope.png"));
        TextureRegion[][] tmp = TextureRegion.split(spriteSheetCicplope, 16, 32);

        TextureRegion[] caminarDerechaCiclopeFrames = new TextureRegion[2];
        caminarDerechaCiclopeFrames[0] = tmp[0][0];
        caminarDerechaCiclopeFrames[1] = tmp[0][1];
        setCaminarDerechaAnim(new Animation<>(0.5f, caminarDerechaCiclopeFrames));
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
