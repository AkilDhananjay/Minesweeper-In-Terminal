import java.util.Scanner;

public class Minesweeper {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Game.board = Tile.InitialiseBoard(8, 8);
        Game.scanner = scanner;
        Game.Number_of_mines = 8;

        Game.SetupBoard(Game.board);
        Game.MinePlacement(Game.Number_of_mines);

        do {
            Game.UpdateBoard(Game.board);

            int choice = Game.UserInput(Game.scanner);

            if(choice == -1){
                System.out.println("Admin commands");
                    int Admin_command = scanner.nextInt();
                    if (Admin_command == 15122010) {
                        Game.revealAll();
                    }
                    continue;
            }
            int[] quordinates = Game.TileSelector();
            int row = quordinates[0];
            int coloumn = quordinates[1];

            switch (choice) {
                case 1:
                    if (Game.board[row][coloumn].Mine) {
                        Game.Hit_A_Mine();
                        System.exit(0);
                    } else {
                        Game.reveal(row, coloumn);
                    }
                    break;

                case 2:
                    Game.placeFlag(row, coloumn);
                    break;

                case 3:
                    Game.RemoveFlag(row, coloumn);
                    break;
                case 0:
                    System.out.println("Exitting game");
                    System.exit(0);        
            }
            Game.GAME_OVER = Game.CheckWin();
        } while (!Game.GAME_OVER);
    }
}
