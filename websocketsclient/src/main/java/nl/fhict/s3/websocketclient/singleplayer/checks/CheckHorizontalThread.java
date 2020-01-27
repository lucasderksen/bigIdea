package nl.fhict.s3.websocketclient.singleplayer.checks;
import nl.fhict.s3.websocketclient.singleplayer.Disc;

public class CheckHorizontalThread implements Runnable {
    private volatile boolean hasWon;
    private Disc[][] grid;
    private int columns;

    public CheckHorizontalThread(Disc[][] grid, int columns) {
        this.grid = grid;
        this.columns = columns;
    }

    @Override
    public void run() {
        for (int j = 0; j < columns - 1; j++) {

            int inARowRed = 0;
            int inARowYellow = 0;
            for (Disc[] forEachColumn : grid) {
                Disc aDisc = forEachColumn[j];

                if (aDisc != null && inARowRed + inARowYellow < 4) {
                    if (aDisc.isRed()) {
                        inARowYellow = 0;
                        inARowRed++;
                    } else {
                        inARowRed = 0;
                        inARowYellow++;
                    }

                }
            }
            if (inARowRed >= 4) {
                hasWon = true;
            } else if (inARowYellow >= 4) {
                hasWon = true;
            }
        }
        if(!hasWon){
            hasWon = false;
        }
    }

    public boolean getBool() {
        return hasWon;
    }
}
