package io.github.EarthDigger;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
//public class EarthDigger extends ApplicationAdapter implements ApplicationListener {
public class EarthDigger extends InputAdapter implements ApplicationListener {
    private float deltaTime = 0;
    private float distance = 0;
    OrthographicCamera camera;
    SpriteBatch batch;
    Sprite sprite;


    @Override
    public boolean keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.SPACE:
                System.out.println("SPACE");
                break;
            case Input.Keys.P:
                System.out.println("P");
                break;
            case Input.Keys.A:
                System.out.println("A");
                break;
            case Input.Keys.D:
                System.out.println("D");
                break;
        }

        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("CLICK: " + screenX + ", " + screenY);
        return true;
    }

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);
        camera = new OrthographicCamera(700, 400);
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture("pescao.png"));
        sprite.setPosition(-350, -200);
        sprite.setSize(100, 100);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        deltaTime = Gdx.graphics.getDeltaTime();

        distance = distance + (20*deltaTime);


        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        sprite.getTexture().dispose();
    }

    @Override
    public void resize (int width, int height){


    }

}
