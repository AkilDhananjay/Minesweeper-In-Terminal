import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {
    static Scanner scanner;

    public static boolean GAME_OVER = false;
    static int Number_of_mines;
    static int Flags_Availabe = Number_of_mines;

    static Tile board[][];
    static int[] Mine_map = new int[Number_of_mines];
    static ArrayList<Integer> Flag_Map = new ArrayList<>();

    public static void UpdateBoard(Tile[][] board) {

        for (int row = 0; row < board.length + 1; row++) {
            for (int coloumn = 0; coloumn < ((row == board.length) ? (board.length + 1)
                    : (board[row].length + 1)); coloumn++) {
                if (row == board.length) {
                    for (int coloumn0 = 1; coloumn0 <= board.length; coloumn0++) {
                        Utilites.printWithTimer("   " + coloumn0 + "   ", coloumn0);
                    }
                    break;
                }

                String text = "";
                if (coloumn == board[row].length) {
                    text = String.valueOf(" " + (row + 1));
                } else if (board[row][coloumn].Flag) {
                    text = " [ " + Color.BLUE + "F" + Color.RESET + " ] ";
                } else if (board[row][coloumn].hidden) {
                    text = " [ " + Color.ORANGE + Color.Hidden + Color.RESET + " ] ";

                } else if (board[row][coloumn].Mine) {
                    text = " [ " + Color.RED + "X" + Color.RESET + " ] ";
                } else {

                    int bombs_nearby = Tile.nearby_mines(board, row, coloumn);
                    if (bombs_nearby > 0) {
                        switch (bombs_nearby) {
                            case 1 -> text = " [ " + Color.BLUE + bombs_nearby + Color.RESET + " ] ";
                            case 2 -> text = " [ " + Color.GREEN + bombs_nearby + Color.RESET + " ] ";
                            case 3 -> text = " [ " + Color.RED + bombs_nearby + Color.RESET + " ] ";
                            case 4 -> text = " [ " + Color.PURPLE + bombs_nearby + Color.RESET + " ] ";
                            case 5 -> text = " [ " + Color.DARK_RED + bombs_nearby + Color.RESET + " ] ";
                            case 6 -> text = " [ " + Color.CYAN + bombs_nearby + Color.RESET + " ] ";
                            case 7 -> text = " [ " + Color.YELLOW + bombs_nearby + Color.RESET + " ] ";
                            case 8 -> text = " [ " + Color.PINK + bombs_nearby + Color.RESET + " ] ";
                            default -> {
                                System.out.println("More that 8 mine ERROR");
                                System.exit(0);
                            }

                        }
                    }

                    else {
                        text = " [   ] ";
                    }

                }

                Utilites.printWithTimer(text, 1);
            }
            System.out.println();
        }
        System.out.println(Color.BLUE + "Flags :" + Flags_Availabe + Color.RESET);
    }

    public static void reveal(int row, int coloumn) {
        if (row < 0 || row >= board.length || coloumn < 0 || coloumn >= board[row].length) {
            return;
        }

        Tile current = board[row][coloumn];
        if (!current.hidden || current.Mine) {
            return;
        }

        current.hidden = false;
        if (current.mine_number > 0) {
            return;
        }

        for (int Row = row - 1; Row <= row + 1; Row++) {
            for (int Coloumn = coloumn - 1; Coloumn <= coloumn + 1; Coloumn++) {
                if (Row == row && Coloumn == coloumn) {
                    continue;
                }

                if ((Row >= 0 && Row < board.length)
                        && (Coloumn >= 0 && Coloumn < board[Row].length)
                        && board[Row][Coloumn].hidden) {
                    reveal(Row, Coloumn);
                }
            }
        }
    }

    public static boolean CheckWin() {
        for (int index = 0; index < Mine_map.length; index++) {
            if (!Flag_Map.contains(Mine_map[index])) {
                return false;
            }
        }

        if (Flags_Availabe == 0) {
            revealAll();
            System.out.println(Color.RED + "  GAMW WON  " + Color.RESET);
            UpdateBoard(board);
            return true;
        }
        return false;
    }

    public static void revealAll() {
        for (int row = 0; row < board.length; row++) {
            for (int coloumn = 0; coloumn < board[row].length; coloumn++) {
                board[row][coloumn].hidden = false;
            }
        }
    }

    public static int[] TileSelector() {
        System.out.print("Enter Row :");
        int x = scanner.nextInt();

        if (x > board.length) {
            System.out.println("Row value can't be greater that board length");
            TileSelector();
        }
        System.out.print("Enter coloumn :");
        int y = scanner.nextInt();
        if (y > board[x - 1].length) {
            System.out.println("Coloumn value can't be greater that board length");
            TileSelector();
        }
        int[] quordinates = { x - 1, y - 1 };
        return quordinates;

    }

    public static void placeFlag(int row, int coloumn) {
        if (board[row][coloumn].Flag) {
            System.out.println("Tile already has a flag");
            System.out.println("if you want to remove flag type (yes)");
            System.out.print(":");
            String choice = scanner.next().trim().toLowerCase();
            if (choice.equals("yes")) {
                RemoveFlag(row, coloumn);
            }
        } else {
            board[row][coloumn].Flag = true;
            Flag_Map.add(board[row][coloumn].TileNumber);
            Flags_Availabe--;
        }
    }

    public static void RemoveFlag(int row, int coloumn) {
        if (!board[row][coloumn].Flag) {
            System.out.println("Tile already does'nt has a flag");
            System.out.println("if you want to add flag type (yes)");
            System.out.print(":");
            String choice = scanner.next().trim().toLowerCase();
            if (choice.equals("yes")) {
                placeFlag(row, coloumn);
            }
        } else {
            board[row][coloumn].Flag = false;
            Flag_Map.removeIf(tile -> tile == board[row][coloumn].TileNumber);
            Flags_Availabe++;
        }
    }

    public static void SetupBoard(Tile[][] board) {
        Tile.assingTileNumber(board);
        Tile.CerateTileMap(board);
    }

    public static void MinePlacement(int numberOfMines) {
        if (numberOfMines > (board.length * board[0].length)) {
            System.out.println("Number of mine can not be more than tiles");
            System.exit(0);
        }

        // update state now that we know how many mines there will be
        Number_of_mines = numberOfMines;
        Flags_Availabe = numberOfMines;
        Mine_map = new int[Number_of_mines];

        placeMines(board, Number_of_mines);
        Tile.assignMineNumbers(board);
    }

    public static void placeMines(Tile[][] board, int Number_of_mines) {
        Random random = new Random();

        outer: for (int MinesToPlace = 0; MinesToPlace < Number_of_mines; MinesToPlace++) {
            int TileToPlaceMine = random.nextInt(board.length * board[0].length) + 1;
            for (int DuplicateMines = 0; DuplicateMines < MinesToPlace; DuplicateMines++) {
                if (TileToPlaceMine == Mine_map[DuplicateMines]) {
                    --MinesToPlace;
                    continue outer;
                }
            }

            Mine_map[MinesToPlace] = TileToPlaceMine;
            Tile.findTile(TileToPlaceMine).Mine = true;
        }
    }

    public static int UserInput(Scanner scanner) {
        System.out.println("What do you wan to do?");
        System.out.println();
        System.out.println("(0) Exit Game");
        System.out.println("(1) Mine");
        System.out.println("(2) Place Flag");
        System.out.println("(3) Remove Flag");
        System.out.println("(4+) Nothing");
        System.out.print(":");
        return scanner.nextInt();

    }

    public static void Hit_A_Mine() {
        System.out.println("You hit a mine -> GAME OVER");
        revealAll();
        UpdateBoard(board);
        GAME_OVER = true;
    }

    
}
