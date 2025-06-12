package io.github.EarthDigger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class Muerte implements Screen {
    private EarthDigger game;
    private Stage stage;
    private Texture muertoTexture;

    public Muerte(EarthDigger game) {
        this.game = game;
    }

    @Override
    public void show() {

        muertoTexture = new Texture(Gdx.files.internal("HASMUERTO.png"));

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        TextureAtlas atlas = new TextureAtlas("craftacular-ui.atlas");
        Skin skin = new Skin(Gdx.files.internal("craftacular-ui.json"), atlas);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        TextButton playButton = new TextButton("Reiniciar", skin);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Juego(game));
                Audios.getInstance().pausarMusica();
            }
        });



        TextButton exitButton = new TextButton("Salir", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add().height(muertoTexture.getHeight()).row();
        table.add(playButton).pad(10).row();
        table.add(exitButton).pad(10);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(muertoTexture, Gdx.graphics.getWidth()/2f - (float) muertoTexture.getWidth() /2, (float) muertoTexture.getHeight() /2 + muertoTexture.getHeight(), muertoTexture.getWidth(), muertoTexture.getHeight());
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        muertoTexture.dispose();
    }

    @Override public void pause() {
    }
    @Override public void resume() {}
    @Override public void hide() {}
}
