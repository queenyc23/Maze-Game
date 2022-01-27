package byow.WorldGeneration;
import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.util.Random;

/**
 * Draws an empty world.
 * Connect rooms + halls to create a non-empty world.
 *
 * Keeps track of positions so that rooms + halls don't overlap!
 * use a list + check if it contains the positions alrd...
 */
public class WorldGenerator {
    private final int WIDTH = 80;
    private final int HEIGHT = 40;
    private long seed;
    private Random r;
    private int flowerCount = 0;
    TETile[][] world = new TETile[WIDTH][HEIGHT];

    public WorldGenerator(long seed) {
        this.seed = seed;
    }

    private void flowerPower(int x, int y) {
        if (world[x + 1][y + 1] != Tileset.NOTHING && world[x + 1][y + 1]
                != Tileset.WALL && world[x + 1][y + 1] != Tileset.AVATAR) {
            world[x + 1][y + 1] = Tileset.FLOWER;
            flowerCount += 1;
        }
    }
    public TETile[][] worldGeneration() {
        r = new Random(seed);
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        Room roomPrev = null;
        Position roomPrevPosition = null;
        int numRooms = RandomUtils.uniform(r, 80000, 150000);
        for (int i = numRooms; i != 0; i--) {
            int width = RandomUtils.uniform(r, 4, 10);
            int height = RandomUtils.uniform(r, 4, 10);
            int x = RandomUtils.uniform(r, 10, 80 - width - 10);
            int y = RandomUtils.uniform(r, 10, 40 - height - 10);
            if (flowerCount < 20) {
                flowerPower(x, y);
            }
            Room room1 = new Room(x, y, width, height, world);
            Position room1Position = new Position(x, y, width, height);
            width = RandomUtils.uniform(r, 4, 10);
            height = RandomUtils.uniform(r, 4, 10);
            x = RandomUtils.uniform(r, 10, 80 - width - 10);
            y = RandomUtils.uniform(r, 10, 40 - height - 10);
            Room room2 = new Room(x, y, width, height, world);
            Position room2Position = new Position(x, y, width, height);
            if (!overlapBetweenR1R2(room1Position, room2Position)) {
                if (!overlap(room1Position) && !overlap(room2Position)) {
                    room1.drawRoom();
                    room2.drawRoom();
                    findPath(room1Position, room2Position);
                    findPath(room2Position, roomPrevPosition);
                }
            }
            roomPrevPosition = room2Position;
        }
        return world;
    }

    private boolean overlapBetweenR1R2(Position room1Position, Position room2Position) {
        int room1X = room1Position.getX();
        int room2X = room2Position.getX();
        int room1Y = room1Position.getY();
        int room2Y = room2Position.getY();
        if (room1X == room2X && room1Y == room2Y) {
            return true;
        }
        for (int i = room1Position.getHeight(); i >= 0; i--) {
            for (int j = room1Position.getWidth(); j >= 0; j--) {
                for (int k = room2Position.getHeight(); k >= 0; k--) {
                    for (int l = room2Position.getWidth(); l >= 0; l--) {
                        if (room1X + j == room2X + l) {
                            if (room1Y + i == room2Y + k) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean overlap(Position position) {
        for (int i = position.getWidth(); i != 0; i--) {
            for (int j = position.getHeight(); j != 0; j--) {
                if (world[position.getX() + i][position.getY() + j] != Tileset.NOTHING) {
                    return true;
                }
            }
        }
        return false;
    }

    private void findPath(Position room1Position, Position room2Position) {
        if (room1Position == null || room2Position == null) {
            return;
        }
        int room1X = room1Position.getX();
        int room2X = room2Position.getX();
        int room1Y = room1Position.getY();
        int room2Y = room2Position.getY();
        if (room1X == room2X) {
            if (room1Y > room2Y) {
                onlyTop(room1Position, room2Position);
            } else {
                onlyTop(room2Position, room1Position);
            }
        }
        if (room1Y == room2Y) {
            if (room1X > room2X) {
                onlyRight(room1Position, room2Position);
            } else {
                onlyRight(room2Position, room1Position);
            }
        }
        if (room1X > room2X) {
            if (room1Y > room2Y) {
                topRight(room1Position, room2Position);
            } else {
                topLeft(room2Position, room1Position);
            }
        } else if (room1X < room2X) {
            if (room1Y < room2Y) {
                topRight(room2Position, room1Position);
            } else {
                topLeft(room1Position, room2Position);
            }
        }
    }

    private void onlyTop(Position top, Position bottom) {
        int beginFromX = bottom.getX();
        int beginFromY = bottom.getY() + bottom.getHeight();
        int differenceY = top.getY() - beginFromY;
        Room hallway = new Room(beginFromX, beginFromY, 3, differenceY + 1, world);
        hallway.drawRoom();
        if (surrounded(beginFromX + 1, beginFromY)) {
            world[beginFromX + 1][beginFromY] = Tileset.FLOOR;
        }
        if (surrounded(beginFromX + 1, beginFromY)) {
            world[beginFromX + 1][beginFromY] = Tileset.FLOOR;
        }
        if (surrounded(beginFromX + 1, top.getY())) {
            world[beginFromX + 1][top.getY()] = Tileset.FLOOR;
        }
    }

    private void onlyRight(Position right, Position left) {
        int beginFromX = left.getX() + left.getWidth();
        int beginFromY = left.getY();
        int differenceX = right.getX() - beginFromX;
        Room hallway = new Room(beginFromX, beginFromY, differenceX + 1, 3, world);
        if (surrounded(beginFromX, beginFromY + 1)) {
            world[beginFromX][beginFromY + 1] = Tileset.FLOOR;
        }
        if (surrounded(right.getX(), beginFromY + 1)) {
            world[right.getX()][beginFromY + 1] = Tileset.FLOOR;
        }
    }

    private void topLeft(Position topLeft, Position bottomRight) {
        int beginFromY = bottomRight.getY();
        int beginFromX = topLeft.getX();
        int differenceY = topLeft.getY() - bottomRight.getY();
        Room hallwayFirst = new Room(beginFromX, beginFromY, 3, differenceY + 1, world);
        hallwayFirst.drawRoom();
        int startFromX = topLeft.getX();
        int startFromY = bottomRight.getY();
        int differenceX = bottomRight.getX() - topLeft.getX();
        Room hallwaySecond = new Room(startFromX, startFromY, differenceX + 1, 3, world);
        hallwaySecond.drawRoom();
        if (surrounded(beginFromX + 1, topLeft.getY())) {
            world[beginFromX + 1][topLeft.getY()] = Tileset.FLOOR;
        }
        if (surrounded(bottomRight.getX(), bottomRight.getY() + 1)) {
            world[bottomRight.getX()][bottomRight.getY() + 1] = Tileset.FLOOR;
        }
        if (surrounded(topLeft.getX() + 1, bottomRight.getY() + 2)) {
            world[topLeft.getX() + 1][bottomRight.getY() + 2] = Tileset.FLOOR;
        }
    }

    private void topRight(Position topRight, Position bottomLeft) {
        int beginFromY = bottomLeft.getY();
        int beginFromX = bottomLeft.getX() + bottomLeft.getWidth() - 1;
        int differenceX = topRight.getX() - beginFromX;
        Room hallwayFirst = new Room(beginFromX, beginFromY, differenceX + 3, 3, world);
        hallwayFirst.drawRoom();
        int startFromX = topRight.getX();
        int startFromY = bottomLeft.getY();
        int differenceY = topRight.getY() - bottomLeft.getY();
        Room hallwaySecond = new Room(startFromX, startFromY, 3, differenceY + 1, world);
        hallwaySecond.drawRoom();
        if (surrounded(beginFromX, beginFromY + 1)) {
            world[beginFromX][beginFromY + 1] = Tileset.FLOOR;
        }
        if (surrounded(topRight.getX() + 1, topRight.getY())) {
            world[topRight.getX() + 1][topRight.getY()] = Tileset.FLOOR;
        }
        if (surrounded(topRight.getX() + 1, topRight.getY() - 1)) {
            world[topRight.getX() + 1][topRight.getY() - 1] = Tileset.FLOOR;
        }
    }

    private boolean surrounded(int x, int y) {
        if (world[x + 1][y] == Tileset.FLOOR) {
            if (world[x - 1][y] == Tileset.FLOOR) {
                return true;
            }
        }
        if (world[x][y + 1] == Tileset.FLOOR) {
            if (world[x][y - 1] == Tileset.FLOOR) {
                return true;
            }
        }
        return false;
    }
}
