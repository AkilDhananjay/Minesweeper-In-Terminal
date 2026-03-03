public class Tile {
    public boolean Mine = false;
    public boolean Flag = false;
    public int TileNumber;
    public boolean hidden = true;
    private static Tile[] TileMap = null;
    public int mine_number;

    public static void CerateTileMap(Tile[][] board) {
        int index = 0;
        TileMap = new Tile[board.length * board[0].length];
        for (int row = 0; row < board.length; row++) {
            for (int coloumn = 0; coloumn < board[row].length; coloumn++) {
                TileMap[index++] = board[row][coloumn];
            }
        }
    }

    public static void assingTileNumber(Tile[][] TileArray) {
        int index = 1;
        for (int row = 0; row < TileArray.length; row++) {
            for (int coloumn = 0; coloumn < TileArray[row].length; coloumn++) {
                TileArray[row][coloumn].TileNumber = index++;
            }
        }
    }

    public static Tile[][] InitialiseBoard(int row, int coloumn) {
        Tile board[][] = new Tile[row][coloumn];

        for (int rowNumber = 0; rowNumber < board.length; rowNumber++) {
            for (int coloumnNumber = 0; coloumnNumber < board[rowNumber].length; coloumnNumber++) {
                board[rowNumber][coloumnNumber] = new Tile();
            }
        }
        return board;
    }

    public static Tile findTile(int TileNumber) {
        int high = TileMap.length - 1;
        int low = 0;

        while (low <= high) {
            int mid = (high + low) / 2;

            if (TileMap[mid].TileNumber > TileNumber)
                high = mid - 1;

            else if (TileMap[mid].TileNumber < TileNumber)
                low = mid + 1;

            else if (TileMap[mid].TileNumber == TileNumber)
                return TileMap[mid];

        }
        System.out.println("Tile Number doesn't exist");
        return null;
    }

    public static int nearby_mines(Tile[][] board, int row, int coloumn) {

        int bombs = 0;
        for (int mineInRow = row - 1; mineInRow <= row + 1; mineInRow++) {
            for (int mineInColoumn = coloumn - 1; mineInColoumn <= coloumn + 1; mineInColoumn++) {
                if (mineInRow == row && mineInColoumn == coloumn)
                    continue;

                if ((mineInRow >= 0 && mineInRow < board.length)
                        && (mineInColoumn >= 0 && mineInColoumn < board[mineInRow].length)) {
                    if (board[mineInRow][mineInColoumn].Mine == true) {
                        bombs++;
                    }
                }

                
            }
        }
        return bombs;
    }

    public static void assignMineNumbers(Tile[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int coloumn = 0; coloumn < board[row].length; coloumn++) {
                if (board[row][coloumn].Mine) {
                    board[row][coloumn].mine_number = -1;
                } else {
                    board[row][coloumn].mine_number = nearby_mines(board, row, coloumn);
                }
            }
        }
    }
}
