package io.github.EarthDigger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Enemy2 extends Personaje {
    private float velocidadX = 50f;
    private Animation<TextureRegion> caminarDerechaAnim;
    private float stateTime;
    private Rectangle enemigo2hitbox;

    public Enemy2(String sprite, float x, float y) {
        super(sprite, x, y);
        caminarDerechaAnim = Assets.getEnemy2WalkRight();
        stateTime = 0f;
        enemigo2hitbox = new Rectangle(x, y, 16, 32);
    }


    public void update(float delta) {
        setPosicion(getX() + velocidadX * delta, getY());
        enemigo2hitbox.setPosition(getX(), getY());
        stateTime += delta;
    }

    public void dibujar(SpriteBatch batch) {
        TextureRegion frame = caminarDerechaAnim.getKeyFrame(stateTime, true);
        batch.draw(frame, getX(), getY(), 16, 32);
    }

    public Rectangle getHitbox() {
        return enemigo2hitbox;
    }
}
