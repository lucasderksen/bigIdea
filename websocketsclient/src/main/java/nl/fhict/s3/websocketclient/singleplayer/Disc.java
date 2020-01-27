package nl.fhict.s3.websocketclient.singleplayer;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Disc extends Circle {
    private final boolean red;

    public Disc(boolean red, int TILE_SIZE) {
        super(TILE_SIZE / (double)2, red ? Color.RED : Color.YELLOW);
        this.red = red;

        setCenterX(TILE_SIZE / (double)2);
        setCenterY(TILE_SIZE / (double)2);
    }

    public boolean isRed() {
        return red;
    }

}