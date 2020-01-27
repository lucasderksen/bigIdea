package nl.fhict.s3.websocketclient.singleplayer.checks;


import nl.fhict.s3.websocketclient.singleplayer.Disc;

public class CheckDiagonalThread implements Runnable {
    private volatile boolean hasWon;
    private Disc[][] grid;
    private int columns;
    private int rows;

    public CheckDiagonalThread(Disc[][] grid, int columns, int rows) {
        this.grid = grid;
        this.columns = columns;
        this.rows = rows;
    }

    @Override
    public void run() {
        for (int j = 0; j < columns; j++) {
            int gridX = j;
            int gridY = 0;

            int inARowRed = 0;
            int inARowYellow = 0;

            if (CheckFourInARowDiagonal(gridX, gridY, inARowRed, inARowYellow)) hasWon = true;

        }
        for (int k = 0; k < rows; k++) {
            int gridX = 0;
            int gridY = k;

            int inARowRed = 0;
            int inARowYellow = 0;

            if (CheckFourInARowDiagonal(gridX, gridY, inARowRed, inARowYellow)) hasWon = true;
        }
        if(!hasWon){
            hasWon = false;
        }
    }

    private boolean CheckFourInARowDiagonal(int gridX, int gridY, int inARowRed, int inARowYellow) {
        while (gridX < columns && gridY < rows) {
            if (grid[gridX][gridY] != null && inARowRed + inARowYellow < 4) {
                if (grid[gridX][gridY].isRed()) {
                    inARowYellow = 0;
                    inARowRed++;
                } else {
                    inARowRed = 0;
                    inARowYellow++;
                }
            }
            gridX++;
            gridY++;
        }

        if (inARowRed >= 4) {
            return true;

        } else if (inARowYellow >= 4) {
            return true;

        }
        return false;
    }

    public boolean getBool() {
        return hasWon;
    }
}
