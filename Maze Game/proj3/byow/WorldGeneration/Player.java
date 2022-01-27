package byow.WorldGeneration;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;

public class Player {

    private TETile[][] world;
    private TETile avatar;
    private long seed;
    private Random r;
    private Position playerPosition;
    private int xPos;
    private int yPos;

    public Player(TETile[][] world, TETile avatar, long seed) {
        this.world = world;
        this.avatar = avatar;
        this.seed = seed;
        r = new Random(seed);
        xPos = r.nextInt((70 - 10) + 1) + 10;
        yPos = r.nextInt((30 - 10) + 1) + 10;
        while (world[xPos][yPos] != Tileset.FLOOR) {
            xPos = r.nextInt((70 - 10) + 1) + 10;
            yPos = r.nextInt((30 - 10) + 1) + 10;
        }
        playerPosition = new Position(xPos, yPos);
    }

    public TETile[][] playerGeneration() {
        world[xPos][yPos] = avatar;
        return world;
    }


    public TETile[][] playerMovement(char c) {
        TETile top = world[xPos][yPos + 1];
        TETile bottom = world[xPos][yPos - 1];
        TETile left = world[xPos - 1][yPos];
        TETile right = world[xPos + 1][yPos];
        switch (c) {
            case 'w':
            case 'W':
                if (top == Tileset.FLOOR) {
                    world[xPos][yPos + 1] = Tileset.AVATAR;
                    world[xPos][yPos] = Tileset.FLOOR;
                    playerPosition = new Position(xPos, yPos + 1);
                    yPos++;
                    return world;
                }
                break;
            case 'A':
            case 'a':
                if (left == Tileset.FLOOR) {
                    world[xPos - 1][yPos] = Tileset.AVATAR;
                    world[xPos][yPos] = Tileset.FLOOR;
                    playerPosition = new Position(xPos - 1, yPos);
                    xPos--;
                    return world;
                }
                break;
            case 'S':
            case 's':
                if (bottom == Tileset.FLOOR) {
                    world[xPos][yPos - 1] = Tileset.AVATAR;
                    world[xPos][yPos] = Tileset.FLOOR;
                    playerPosition = new Position(xPos, yPos - 1);
                    yPos--;
                    return world;
                }
                break;
            case 'D':
            case 'd':
                if (right == Tileset.FLOOR) {
                    world[xPos + 1][yPos] = Tileset.AVATAR;
                    world[xPos][yPos] = Tileset.FLOOR;
                    playerPosition = new Position(xPos + 1, yPos);
                    xPos++;
                    return world;
                }
                break;
            default:
        }
        return world;
    }
}
