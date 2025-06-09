package io.github.EarthDigger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Enemy extends Personaje {
    TextureRegion spriteEnemigo;

    public Enemy(String rutaSpriteSheet, float ancho, float alto) {
        super(rutaSpriteSheet, ancho, alto);

        Texture textura = new Texture(Gdx.files.internal("easteregg.png"));
        spriteEnemigo = new TextureRegion(textura);
    }

    public void seguirAlJugador(Vector2 jugadorPos, float delta) {
        Vector2 direccion = jugadorPos.cpy().sub(posX, posY);
        if (direccion.len() > 1f) {
            direccion.nor();
            float velocidad = 30f;
            posX += direccion.x * velocidad * delta;
            posY += direccion.y * velocidad * delta;
        }
    }


    @Override
    public void dibujar(SpriteBatch batch) {
        batch.draw(spriteEnemigo, getX(), getY(), getAncho(), getAlto());
    }

    @Override
    public void update(float delta) {
        // Si quieres usar animación, podrías usar el update original también
        super.update(delta);
    }
}
