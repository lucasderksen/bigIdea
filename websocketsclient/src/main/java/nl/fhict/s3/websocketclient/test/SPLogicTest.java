package nl.fhict.s3.websocketclient.test;

import nl.fhict.s3.websocketclient.singleplayer.Disc;
import nl.fhict.s3.websocketclient.singleplayer.SPLogic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class spLogicTest {

    private SPLogic spLogicTestClass;

    @BeforeEach
    private void beforeAll() {
        spLogicTestClass = new SPLogic(80, 7, 6);
    }

    @Test
    void fieldSize() {
        int rowsAmount = 0;
        int columAmount = 0;

        Disc[][] discs = spLogicTestClass.getGrid();
        for (Disc[] Rows : discs
        ) {
            rowsAmount++;
            columAmount = Rows.length;

        }
        assertEquals(rowsAmount + columAmount, 7 + 6);
    }


    @Test
    void hasWonTrueVertical() {
        Disc disc = new Disc(true, 80);
        spLogicTestClass.placeDisc(1, 1, disc);
        spLogicTestClass.placeDisc(1, 2, disc);
        spLogicTestClass.placeDisc(1, 3, disc);
        spLogicTestClass.placeDisc(1, 4, disc);

        assertTrue(spLogicTestClass.hasWon());
    }

    @Test
    void hasWonTrueDiagonal() {
        Disc disc = new Disc(true, 80);
        spLogicTestClass.placeDisc(1, 1, disc);
        spLogicTestClass.placeDisc(2, 2, disc);
        spLogicTestClass.placeDisc(3, 3, disc);
        spLogicTestClass.placeDisc(4, 4, disc);

        assertTrue(spLogicTestClass.hasWon());
    }

    @Test
    void hasWonTrueHorizontal() {
        Disc disc = new Disc(true, 80);
        spLogicTestClass.placeDisc(1, 1, disc);
        spLogicTestClass.placeDisc(2, 1, disc);
        spLogicTestClass.placeDisc(3, 1, disc);
        spLogicTestClass.placeDisc(4, 1, disc);

        assertTrue(spLogicTestClass.hasWon());
    }

    @Test
    void hasWonFalse() {
        Disc disc = new Disc(true, 80);
        spLogicTestClass.placeDisc(1, 1, disc);
        spLogicTestClass.placeDisc(4, 2, disc);
        spLogicTestClass.placeDisc(3, 3, disc);
        spLogicTestClass.placeDisc(2, 4, disc);


        assertFalse(spLogicTestClass.hasWon());
    }

    @Test
    void placeDisc() {
        Disc disc = new Disc(true, 80);
        spLogicTestClass.placeDisc(1, 1, disc);
        spLogicTestClass.getGrid()[1][1].isRed();

        assertEquals(spLogicTestClass.getGrid()[1][1].isRed(), disc.isRed());
    }

    @Test
    void rematch() {
        Disc disc = new Disc(true, 80);
        spLogicTestClass.placeDisc(1, 1, disc);
        spLogicTestClass.placeDisc(4, 2, disc);
        spLogicTestClass.placeDisc(3, 3, disc);
        spLogicTestClass.placeDisc(2, 4, disc);

        spLogicTestClass.rematch();
    }

    @Test
    void noDiscPresent() {
        Disc disc = new Disc(true, 80);
        spLogicTestClass.placeDisc(1, 1, disc);

        assertFalse(spLogicTestClass.NoDisc(1, 1));
    }

    @Test
    void noChangesBetweenTurns() {
        Disc disc = new Disc(true, 80);
        spLogicTestClass.placeDisc(4, 2, disc);

        assertEquals(spLogicTestClass.getGrid()[4][2],disc);

    }

}