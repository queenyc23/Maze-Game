package byow.Core;
import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.WorldGeneration.Player;
import byow.WorldGeneration.WorldGenerator;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Engine {
    TERenderer ter = new TERenderer();
    private long seed;
    private boolean gameOn = true;
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        mainMenu();
        InputSource inputSource;
        inputSource = new KeyboardInputSource();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char selection = StdDraw.nextKeyTyped();
                switch (selection) {
                    case ('N'):
                    case ('n'): {
                        seed = Long.parseLong(enterSeed());
                        //newgame(seed);
                        ter.initialize(WIDTH, HEIGHT); //delete for autograder
                        WorldGenerator creation = new WorldGenerator(seed);
                        TETile[][] finalWorldFrame = creation.worldGeneration();
                        Player player = new Player(finalWorldFrame, Tileset.AVATAR, seed);
                        finalWorldFrame = player.playerGeneration();
                        ter.renderFrame(finalWorldFrame); //delete for autograder
                        //TETile[][] hudWorld = newgame(seed);
                        while (gameOn) {
                            hud(finalWorldFrame);
                        }
                        //play(finalWorldFrame, player);
                        break;
                    }
                    case ('L'):
                    case ('l'): {
                        loadgame();
                        break;
                    }
                    case ('Q'):
                    case ('q'): {
                        System.exit(0);
                        break;
                    }
                    default:
                }
            }
        }
    }

    private void flowerTrance(Player player) {

    }

    private TETile[][] newgame(long inputSeed) {
        ter.initialize(WIDTH, HEIGHT);
        WorldGenerator creation = new WorldGenerator(inputSeed);
        TETile[][] finalWorldFrame = creation.worldGeneration();
        Player player = new Player(finalWorldFrame, Tileset.AVATAR, seed);
        finalWorldFrame = player.playerGeneration();
        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }
  /*  WorldGenerator creation = new WorldGenerator(seed);
    TETile[][] finalWorldFrame = creation.worldGeneration();
    Player player = new Player(finalWorldFrame, Tileset.AVATAR, seed);
    finalWorldFrame = player.playerGeneration();
        ter.renderFrame(finalWorldFrame);
        while (inputSource.possibleNextInput()) {
        char movement = inputSource.getNextKey();
        finalWorldFrame = player.playerMovement(movement);
        movementRecord += movement;
        ter.renderFrame(finalWorldFrame);
    }
        return finalWorldFrame; */
   /* private void play(TETile[][] world, Player player) {
        char playerMoves = StdDraw.nextKeyTyped();
        String record = "";
        while (gameOn) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            record += playerMoves;
            for (int i = 0; i < record.length() - 1; i += 1) {
                if ((record.charAt(i) == ':' && record.charAt(i + 1) == 'q')
                        || (record.charAt(i) == ':' && record.charAt(i + 1) == 'Q')) {
                }
            }
        }
        world = player.playerMovement(playerMoves);
        ter.renderFrame(world); //delete for autograder
    } */

    // @Source
    // https://introcs.cs.princeton.edu/java/15inout/MouseFollower.java.html
    //
    public void hud(TETile[][] world) {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        if (mouseX < 0 || mouseX > WIDTH || mouseY > HEIGHT || mouseY < 0) {
            throw new ArrayIndexOutOfBoundsException("");
        }
        String hudtext = world[mouseX][mouseY].description();
        Font hudfont = new Font("Monaco", Font.BOLD, 10);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(hudfont);
        StdDraw.text(WIDTH / 2, HEIGHT - 1, hudtext);
        StdDraw.show();
        ter.renderFrame(world);
    }

    private void loadgame() {
    }

    private void mainMenu() {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT - 10, "CS61B: THE GAME");
        Font font1 = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font1);
        StdDraw.text(WIDTH / 2, HEIGHT - 20, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT - 22, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT - 24, "Quit (Q)");
        StdDraw.show();
    }

    private String enterSeed() {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT - 10, "Enter seed followed by 's' to start game.");
        char userInput = '#';
        String finalSeed = "";
        StdDraw.show();
        while (userInput != 's' && userInput != 'S') {
            if (StdDraw.hasNextKeyTyped()) {
                userInput = StdDraw.nextKeyTyped();
                if (userInput != 's' && userInput != 'S') {
                    finalSeed += String.valueOf(userInput);
                }
                StdDraw.clear(Color.BLACK);
                StdDraw.text(WIDTH / 2, HEIGHT - 10, "Enter seed followed by 's' to start game.");
                StdDraw.text(WIDTH / 2, HEIGHT - 20, finalSeed);
                StdDraw.show();
            }
        }
        return finalSeed;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        InputSource inputSource;
        inputSource = new StringInputDevice(input);
        char c = inputSource.getNextKey();
        /*if (c != 'n' && c != 'N') {
            return null;
        }*/
        if (c == 'L' || c == 'l') {
            loadGame(input.substring(1));
        }
        long type = Character.getNumericValue(inputSource.getNextKey());
        while (inputSource.possibleNextInput()) {
            char inputtedChar = inputSource.getNextKey();
            if (inputtedChar == 's' || inputtedChar == 'S') {
                break;
            }
            long number = Character.getNumericValue(inputtedChar);
            type = type * 10 + number;
        }
        //seed = type;
        String movementRecord = "n";
        movementRecord += Long.toString(type);
        movementRecord += "s";
        WorldGenerator creation = new WorldGenerator(type);
        TETile[][] finalWorldFrame = creation.worldGeneration();
        Player player = new Player(finalWorldFrame, Tileset.AVATAR, type);
        finalWorldFrame = player.playerGeneration();
        while (inputSource.possibleNextInput()) {
            char movement = inputSource.getNextKey();
            if (movement == ':') {
                char quit = inputSource.getNextKey();
                if (quit == 'Q' || quit == 'q') {
                    saveGame(movementRecord);
                    return finalWorldFrame;
                }
            }
            finalWorldFrame = player.playerMovement(movement);
            movementRecord += movement;
        }
      //  ter.initialize(WIDTH, HEIGHT); // DELETE FOR AG  -------------
     //   ter.renderFrame(finalWorldFrame); //DELETE FOR AG  -------------
        return finalWorldFrame;
    }

    private void saveGame(String input) {
        //File worldFiles = Paths.get("byow", "WorldGeneration", "worldFiles").toFile();
        /*if (!worldFiles.exists()) {
            worldFiles.mkdir();
        }*/ //worldFiles,
        File lastSaves = new File("loadedFile.txt");
        if (!lastSaves.exists()) {
            try {
                lastSaves.createNewFile();
                FileWriter myWriter = new FileWriter(lastSaves);
                myWriter.write(input);
                myWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadGame(String newInput) {
        //Paths.get("byow", "WorldGeneration", "worldFiles").toFile();
        File worldFiles = new File("loadedFile.txt");
        //File[] inputFile = worldFiles.listFiles();
        try {
            String input = new String(Files.readAllBytes(Paths.get("loadedFile.txt")));
            /*FileReader fr = new FileReader(worldFiles);
            BufferedReader br = new BufferedReader(fr);
            String input = br.readLine();*/
            interactWithInputString(input + newInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
