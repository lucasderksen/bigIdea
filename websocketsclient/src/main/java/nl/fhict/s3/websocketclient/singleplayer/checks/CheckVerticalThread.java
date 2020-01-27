package nl.fhict.s3.websocketclient.singleplayer.checks;
import nl.fhict.s3.websocketclient.singleplayer.Disc;

public class CheckVerticalThread implements Runnable  {

    private volatile boolean hasWon;
    private Disc[][] grid;

    public CheckVerticalThread(Disc[][] grid) {
        this.grid = grid;
    }

    @Override
    public void run() {
        for (Disc[] forEachColumn : grid) {
            int inARowRed = 0;
            int inARowYellow = 0;
            for (Disc forEachDisc : forEachColumn) {
                if (forEachDisc != null && inARowRed + inARowYellow < 4) {
                    if (forEachDisc.isRed()) {
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
