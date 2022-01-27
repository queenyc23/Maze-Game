package byow.WorldGeneration;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


public class Room {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;
    private int width;
    private int height;
    private int x;
    private int y;
    private TETile[][] world;

    public Room(int x, int y, int width, int height, TETile[][] world) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.world = world;
    }

    public void drawRoom() {
        if (width <= 2 || height <= 2) {
            return;
        }
        drawRoomWalls(Tileset.WALL);
        drawRoomFloor();
    }

    private void drawRoomFloor() {
        for (int i = 1; i < width - 1; i++) {
            for (int c = 1; c < height - 1; c++) {
                world[x + i][y + c] = Tileset.FLOOR;
            }
        }
    }

    private void drawRoomWalls(TETile a) {
        for (int i = 0; i < width; i++) {
            if (world[x + i][y] != Tileset.FLOOR) {
                world[x + i][y] = a;
            }
            if (world[x + i][y + height - 1] != Tileset.FLOOR) {
                world[x + i][y + height - 1] = a;
            }
        }
        for (int c = 0; c < height; c++) {
            if (world[x][y + c] != Tileset.FLOOR) {
                world[x][y + c] = a;
            }
            if (world[x + width - 1][y + c] != Tileset.FLOOR) {
                world[x + width - 1][y + c] = a;
            }
        }
    }
}
