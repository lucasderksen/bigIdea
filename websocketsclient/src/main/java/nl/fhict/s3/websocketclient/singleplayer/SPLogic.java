package nl.fhict.s3.websocketclient.singleplayer;

import nl.fhict.s3.websocketclient.singleplayer.checks.CheckCounterDiagonalThread;
import nl.fhict.s3.websocketclient.singleplayer.checks.CheckDiagonalThread;
import nl.fhict.s3.websocketclient.singleplayer.checks.CheckHorizontalThread;
import nl.fhict.s3.websocketclient.singleplayer.checks.CheckVerticalThread;

public class SPLogic {

    private static int TILE_SIZE;
    private static int COLUMNS;
    private static int ROWS;

    private Disc[][] grid;

    public SPLogic(int TILE_SIZE, int COLUMNS, int ROWS) {

        this.TILE_SIZE = TILE_SIZE;
        this.COLUMNS = COLUMNS;
        this.ROWS = ROWS;
        grid = new Disc[COLUMNS][ROWS];
    }

    public boolean hasWon() {

        // checks in threads
        CheckHorizontalThread horizontal = new CheckHorizontalThread(grid, COLUMNS);
        Thread threadHorizontal = new Thread(horizontal);
        threadHorizontal.start();

        CheckVerticalThread vertical = new CheckVerticalThread(grid);
        Thread threadVertical = new Thread(vertical);
        threadVertical.start();

        CheckDiagonalThread diagonal = new CheckDiagonalThread(grid, COLUMNS, ROWS);
        Thread threadDiagonal = new Thread(diagonal);
        threadDiagonal.start();

        CheckCounterDiagonalThread counterDiagonal = new CheckCounterDiagonalThread(grid, COLUMNS, ROWS);
        Thread threadCounterDiagonal = new Thread(counterDiagonal);
        threadCounterDiagonal.start();

        //blocks until thread is finished .join
        try {
            threadHorizontal.join();
            threadVertical.join();
            threadDiagonal.join();
            threadCounterDiagonal.join();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }


        // if all finished
        // check heel het veld voor 4 op een rij
        if (horizontal.getBool() || vertical.getBool() || diagonal.getBool()) {
            return true;
        }

        return false;


    }

    public boolean rematch() {
        if (hasWon()) {
            return hasWon();
        }
        return false;
    }

    public boolean NoDisc(int column,int row){
        if (grid[column][row] == null){
            return true;
        }
        return false;
    }

    public boolean placeDisc(int column, int row, Disc disc) {

        if(NoDisc(column,row)){
            grid[column][row] = disc;
            System.out.println("Column: " + column + " Row: " + row);

            if (hasWon()) {
                System.out.println("IEMAND HEEFT GEWONNEN");
                return true;

            }
            return false;
        }
        return false;

    }

    public Disc[][] getGrid() {
        return grid;
    }

    public void setGrid(Disc[][] grid) {
        this.grid = grid;
    }
}
