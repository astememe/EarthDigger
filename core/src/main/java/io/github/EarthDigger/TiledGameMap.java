package io.github.EarthDigger;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TiledGameMap extends GameMap {
    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;

    public TiledGameMap() {
        tiledMap = new TmxMapLoader().load("MapaConCollision.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public TiledMapTileLayer getCapaColision() {
        return (TiledMapTileLayer) tiledMap.getLayers().get("Capa de patrones 1");
    }

    @Override
    public void render(OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    @Override public void update(float delta) {}
    @Override public void dispose() { tiledMap.dispose(); }
    @Override public TileType getTileTypeByLocation(int layer, float x, float y) { return null; }
    @Override public TileType getTileTypeByCoordinate(int layer, float x, float y) { return null; }
    @Override public int getWidth() { return 0; }
    @Override public int getHeigth() { return 0; }
    @Override public int getLayer() { return 0; }
}
