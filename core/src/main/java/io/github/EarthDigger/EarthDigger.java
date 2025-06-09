package io.github.EarthDigger;

import com.badlogic.gdx.Game;

public class EarthDigger extends Game {
    @Override
    public void create() {
        this.setScreen(new MenuScreen(this));
    }
}
