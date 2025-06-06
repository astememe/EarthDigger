package io.github.EarthDigger;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Personaje {
    private Sprite sprite;
    private Texture texture;
    private Rectangle personajeHitbox;

    private float velocidadY = 0;
    private float gravedad = -100;
    private boolean saltando = false;
    private int cantSaltos = 0;
    private final float groundY;

    public Personaje(String rutaTextura, float ancho, float alto, float groundY) {
        this.texture = new Texture(rutaTextura);
        this.sprite = new Sprite(texture);
        this.sprite.setSize(ancho, alto);
        this.personajeHitbox = new Rectangle(sprite.getX(), sprite.getY(), ancho, alto);
        this.groundY = groundY;
    }

    public void moverIzquierda(float delta) {
        sprite.setX(sprite.getX() - delta * 32);
    }

    public void moverDerecha(float delta) {
        sprite.setX(sprite.getX() + delta * 32);
    }

    public void saltar() {
        if (cantSaltos < 2) {
            velocidadY = 70;
            saltando = true;
            cantSaltos++;
        }
    }

    public void reiniciarSaltos(float delta) {
        velocidadY += gravedad * delta;
        sprite.setY(sprite.getY() + velocidadY * delta);

        int gravedadLimite = -250;
        if (velocidadY <= 0) {
            gravedad -= 10;
            if (gravedad == -250){
                gravedad = gravedadLimite;
            }
        }
        if (sprite.getY() <= groundY) {
            sprite.setY(groundY);
            velocidadY = 0;
            cantSaltos = 0;
            saltando = false;
            gravedad = -100;
        }

        personajeHitbox.setPosition(sprite.getX(), sprite.getY());
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void dispose() {
        texture.dispose();
    }
    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public void dibujar(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
