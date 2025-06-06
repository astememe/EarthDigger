package io.github.EarthDigger;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Personaje {
    private Sprite sprite;
    private Texture texture;
    private Rectangle personajeHitbox;

    private float velocidadY = 0;
    private float gravedadNormal = -100;
    private float gravedadCaida = -300;
    private boolean saltando = false;
    private int cantSaltos = 0;
    //private final float groundY;



    public Personaje(String rutaTextura, float ancho, float alto /*, float groundY*/) {
        this.texture = new Texture(rutaTextura);
        this.sprite = new Sprite(texture);
        this.sprite.setSize(ancho, alto);
        this.personajeHitbox = new Rectangle(sprite.getX(), sprite.getY(), ancho, alto);
        this.sprite.setY(175);
        //this.groundY = groundY;
    }

    public void moverIzquierda(float delta) {
        sprite.setX(sprite.getX() - delta * 32);
    }

    public void moverDerecha(float delta) {
        sprite.setX(sprite.getX() + delta * 32);
    }

    public void saltar() {
        if (cantSaltos < 2) {
            velocidadY = 85;
            saltando = true;
            cantSaltos++;
        }
    }

    public void reiniciarSaltos(float delta, Array<Rectangle> bloques) {
        // Aplica gravedad (más fuerte si cae)
        if (velocidadY < 0) {
            velocidadY += gravedadCaida * delta;
        } else {
            velocidadY += gravedadNormal * delta;
        }

        // Movimiento vertical propuesto
        float nuevaY = sprite.getY() + velocidadY * delta;

        // Crear una hitbox temporal para comprobar la colisión
        Rectangle nuevaHitbox = new Rectangle(sprite.getX(), nuevaY, sprite.getWidth(), sprite.getHeight());

        boolean sobreBloque = false;

        for (Rectangle bloque : bloques) {
            if (nuevaHitbox.overlaps(bloque) && velocidadY <= 0) {
                // Colisiona con un bloque por abajo
                sprite.setY(bloque.y + bloque.height);
                velocidadY = 0;
                cantSaltos = 0;
                saltando = false;
                sobreBloque = true;
                break;
            }
        }

        if (!sobreBloque) {
            // No hay colisión, aplica movimiento normal
            sprite.setY(nuevaY);
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
