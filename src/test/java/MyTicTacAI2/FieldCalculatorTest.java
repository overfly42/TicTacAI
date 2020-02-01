package MyTicTacAI2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FieldCalculatorTest {
    @Test
    public void testExtractRow() {
        final SingleFieldState[][] field = { { SingleFieldState.Empty, SingleFieldState.Empty, SingleFieldState.Empty },
                { SingleFieldState.X, SingleFieldState.Empty, SingleFieldState.O },
                { SingleFieldState.Empty, SingleFieldState.Empty, SingleFieldState.Empty } };
        final SingleFieldState[] result = FieldCalculator.extractRow(field, 1);
        assertNotNull(result);
        assertEquals(result.length, field[1].length);
        for (int i = 0; i < result.length; i++) {
            assertNotNull(result[i]);
            assertEquals(field[1][i], result[i]);
        }
    }

    @Test
    public void testExtractCol() {
        final SingleFieldState[][] field = { { SingleFieldState.Empty, SingleFieldState.Empty, SingleFieldState.Empty },
                { SingleFieldState.X, SingleFieldState.Empty, SingleFieldState.O },
                { SingleFieldState.Empty, SingleFieldState.Empty, SingleFieldState.Empty } };
        final SingleFieldState[] result = FieldCalculator.extractCol(field, 2);
        assertNotNull(result);
        assertEquals(result.length, field[1].length);
        for (int i = 0; i < result.length; i++) {
            assertNotNull(result[i]);
            assertEquals(field[i][2], result[i]);
        }
    }

    @Test
    public void testExtractMainDiagonal() {
        final SingleFieldState[][] field = { { SingleFieldState.X, SingleFieldState.Empty, SingleFieldState.O },
                { SingleFieldState.X, SingleFieldState.O, SingleFieldState.O },
                { SingleFieldState.Empty, SingleFieldState.Empty, SingleFieldState.X } };
        SingleFieldState[] expected = { SingleFieldState.X, SingleFieldState.O, SingleFieldState.X };
        final SingleFieldState[] result = FieldCalculator.exttactMainDiagonal(field);
        assertNotNull(result);
        assertEquals(result.length, field[1].length);
        for (int i = 0; i < result.length; i++) {
            assertNotNull(result[i]);
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testExtractAntiDiagonal() {
        final SingleFieldState[][] field = { { SingleFieldState.X, SingleFieldState.Empty, SingleFieldState.O },
                { SingleFieldState.X, SingleFieldState.O, SingleFieldState.O },
                { SingleFieldState.Empty, SingleFieldState.Empty, SingleFieldState.X } };
        SingleFieldState[] expected = { SingleFieldState.Empty, SingleFieldState.O, SingleFieldState.O };
        final SingleFieldState[] result = FieldCalculator.extractAntiDiagnal(field);
        assertNotNull(result);
        assertEquals(result.length, field[1].length);
        for (int i = 0; i < result.length; i++) {
            assertNotNull(result[i]);
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testCalculateField() {
        final SingleFieldState[][] field = { { SingleFieldState.X, SingleFieldState.Empty, SingleFieldState.O },
                { SingleFieldState.X, SingleFieldState.O, SingleFieldState.O },
                { SingleFieldState.Empty, SingleFieldState.Empty, SingleFieldState.X } };
        /*
         * row(0)->x:0|o:0 row(1)->x:-1|o:1 row(2) x:1|o:-1 col(2) x:-1|o:1
         * col(1)x:-1|o:1 col(0) x:2|o:-4 Main Diagonal ->x:1|o:-1 Anti Diagonal
         * ->x:-4|o:2 Sum(o) = -1 Sum(x) = -3
         */
        int fieldValueForX = -3;
        int fieldValueForO = -1;
        assertEquals(fieldValueForX, FieldCalculator.calculateFieldValue(field, SingleFieldState.X));
        assertEquals(fieldValueForO, FieldCalculator.calculateFieldValue(field, SingleFieldState.O));
    }

    @Test
    public void testEndOfGame() {
        SingleFieldState[][] field = { { SingleFieldState.X, SingleFieldState.O, SingleFieldState.X },
                { SingleFieldState.O, SingleFieldState.X, SingleFieldState.X },
                { SingleFieldState.O, SingleFieldState.X, SingleFieldState.O } };
        assertTrue(FieldCalculator.gameOver(field));
        field[0][0] = SingleFieldState.Empty;
        assertFalse(FieldCalculator.gameOver(field));
    }
@Test
    public void testWinner() {
        SingleFieldState[][] field = { { SingleFieldState.X, SingleFieldState.O, SingleFieldState.X },
                { SingleFieldState.O, SingleFieldState.X, SingleFieldState.X },
                { SingleFieldState.O, SingleFieldState.X, SingleFieldState.O } };
        assertEquals(SingleFieldState.Empty, FieldCalculator.getWinner(field));
        field[0][0] = SingleFieldState.O;
        assertEquals(SingleFieldState.O, FieldCalculator.getWinner(field));
        field[0][0] = SingleFieldState.X;
        field[0][2] = SingleFieldState.O;
        field[0][1] = SingleFieldState.X;
        assertEquals(SingleFieldState.X, FieldCalculator.getWinner(field));
        
    }
}