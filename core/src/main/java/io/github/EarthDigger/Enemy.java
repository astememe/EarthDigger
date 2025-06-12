package io.github.EarthDigger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends Personaje {
    private boolean mirandoDerecha = true;
    private boolean moviendose = false;
    private float stateTime = 0f;

    private Animation<TextureRegion> caminarDerechaSlimeAnim;
    private Animation<TextureRegion> caminarIzquierdaSlimeAnim;
    private Animation<TextureRegion> quietoSlimeAnim;
    private TextureRegion frameActual;

    public Enemy(String rutaSpriteSheet, float ancho, float alto) {
        super(rutaSpriteSheet, ancho, alto);

        Texture spriteSheet = new Texture(Gdx.files.internal(rutaSpriteSheet));
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, 16, 16);

        // Animación caminar derecha
        TextureRegion[] caminarDerechaSlimeFrames = new TextureRegion[2];
        caminarDerechaSlimeFrames[0] = tmp[0][0];
        caminarDerechaSlimeFrames[1] = tmp[0][1];
        caminarDerechaSlimeAnim = new Animation<>(0.5f, caminarDerechaSlimeFrames);

        // Animación caminar izquierda
        TextureRegion[] caminarIzquierdaSlimeFrames = new TextureRegion[2];
        caminarIzquierdaSlimeFrames[0] = tmp[1][0];
        caminarIzquierdaSlimeFrames[1] = tmp[1][1];
        caminarIzquierdaSlimeAnim = new Animation<>(0.5f, caminarIzquierdaSlimeFrames);

        // Animación quieto
        TextureRegion[] quietoSlimeFrames = new TextureRegion[2];
        quietoSlimeFrames[0] = tmp[2][0];
        quietoSlimeFrames[1] = tmp[2][1];
        quietoSlimeAnim = new Animation<>(1f, quietoSlimeFrames);
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

    public void bajoTierra() {

    }

    @Override
    public void dibujar(SpriteBatch batch) {
        batch.draw(getFrameActual(), getX(), getY(), getAncho(), getAlto());
    }
}
