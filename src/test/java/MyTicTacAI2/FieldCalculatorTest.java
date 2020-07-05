package MyTicTacAI2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import MyTicTacAI2.Game.FieldState;
import MyTicTacAI2.utils.FieldCalculator;

public class FieldCalculatorTest {
    @Test
    public void testExtractRow() {
        final FieldState[][] field = { { FieldState.Empty, FieldState.Empty, FieldState.Empty },
                { FieldState.PlayerA, FieldState.Empty, FieldState.PlayerB },
                { FieldState.Empty, FieldState.Empty, FieldState.Empty } };
        final FieldState[] result = FieldCalculator.extractRow(field, 1);
        assertNotNull(result);
        assertEquals(result.length, field[1].length);
        for (int i = 0; i < result.length; i++) {
            assertNotNull(result[i]);
            assertEquals(field[1][i], result[i]);
        }
    }

    @Test
    public void testExtractCol() {
        final FieldState[][] field = { { FieldState.Empty, FieldState.Empty, FieldState.Empty },
                { FieldState.PlayerA, FieldState.Empty, FieldState.PlayerB },
                { FieldState.Empty, FieldState.Empty, FieldState.Empty } };
        final FieldState[] result = FieldCalculator.extractCol(field, 2);
        assertNotNull(result);
        assertEquals(result.length, field[1].length);
        for (int i = 0; i < result.length; i++) {
            assertNotNull(result[i]);
            assertEquals(field[i][2], result[i]);
        }
    }

    @Test
    public void testExtractMainDiagonal() {
        final FieldState[][] field = { { FieldState.PlayerA, FieldState.Empty, FieldState.PlayerB },
                { FieldState.PlayerA, FieldState.PlayerB, FieldState.PlayerB },
                { FieldState.Empty, FieldState.Empty, FieldState.PlayerA } };
        FieldState[] expected = { FieldState.PlayerA, FieldState.PlayerB, FieldState.PlayerA };
        final FieldState[] result = FieldCalculator.exttactMainDiagonal(field);
        assertNotNull(result);
        assertEquals(result.length, field[1].length);
        for (int i = 0; i < result.length; i++) {
            assertNotNull(result[i]);
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testExtractAntiDiagonal() {
        final FieldState[][] field = { { FieldState.PlayerA, FieldState.Empty, FieldState.PlayerB },
                { FieldState.PlayerA, FieldState.PlayerB, FieldState.PlayerB },
                { FieldState.Empty, FieldState.Empty, FieldState.PlayerA } };
        FieldState[] expected = { FieldState.Empty, FieldState.PlayerB, FieldState.PlayerB };
        final FieldState[] result = FieldCalculator.extractAntiDiagnal(field);
        assertNotNull(result);
        assertEquals(result.length, field[1].length);
        for (int i = 0; i < result.length; i++) {
            assertNotNull(result[i]);
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testCalculateField() {
        final FieldState[][] field = { { FieldState.PlayerA, FieldState.Empty, FieldState.PlayerB },
                { FieldState.PlayerA, FieldState.PlayerB, FieldState.PlayerB },
                { FieldState.Empty, FieldState.Empty, FieldState.PlayerA } };
        /*
         * row(0)->x:0|o:0 row(1)->x:-1|o:1 row(2) x:1|o:-1 col(2) x:-1|o:1
         * col(1)x:-1|o:1 col(0) x:2|o:-4 Main Diagonal ->x:1|o:-1 Anti Diagonal
         * ->x:-4|o:2 Sum(o) = -1 Sum(x) = -3
         */
        int fieldValueForX = -3;
        int fieldValueForO = -1;
        assertEquals(fieldValueForX, FieldCalculator.calculateFieldValue(field, FieldState.PlayerA));
        assertEquals(fieldValueForO, FieldCalculator.calculateFieldValue(field, FieldState.PlayerB));
    }

    @Test
    public void testEndOfGame() {
        FieldState[][] field = { { FieldState.PlayerA, FieldState.PlayerB, FieldState.PlayerA },
                { FieldState.PlayerB, FieldState.PlayerA, FieldState.PlayerA },
                { FieldState.PlayerB, FieldState.PlayerA, FieldState.PlayerB } };
        assertTrue(FieldCalculator.gameOver(field));
        field[0][0] = FieldState.Empty;
        assertFalse(FieldCalculator.gameOver(field));
    }
@Test
    public void testWinner() {
        FieldState[][] field = { { FieldState.PlayerA, FieldState.PlayerB, FieldState.PlayerA },
                { FieldState.PlayerB, FieldState.PlayerA, FieldState.PlayerA },
                { FieldState.PlayerB, FieldState.PlayerA, FieldState.PlayerB } };
        assertEquals(FieldState.Empty, FieldCalculator.getWinner(field));
        field[0][0] = FieldState.PlayerB;
        assertEquals(FieldState.PlayerB, FieldCalculator.getWinner(field));
        field[0][0] = FieldState.PlayerA;
        field[0][2] = FieldState.PlayerB;
        field[0][1] = FieldState.PlayerA;
        assertEquals(FieldState.PlayerA, FieldCalculator.getWinner(field));
        
    }
}