package io.github.EarthDigger;

public enum TileType {

    Cesped(1, true, "Cesped"),
    Tierra(3, true, "Tierra"),
    Piedra(2, true, "Piedra");



    public static int TILE_SIZE = 16;

    private int id;
    private boolean collidable;
    private String name;

    private TileType (int id, boolean collidable, String name){
        this.id = id;
        this.collidable = collidable;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public String getName() {
        return name;
    }
}
