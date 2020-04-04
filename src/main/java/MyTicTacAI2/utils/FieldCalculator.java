package MyTicTacAI2.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import MyTicTacAI2.UI.SingleFieldState;

public class FieldCalculator {
    /**
     * Extracts a Field Row from the field(row/column)
     */

    public static SingleFieldState[] extractRow(SingleFieldState[][] field, int rowId) {
        SingleFieldState[] row = new SingleFieldState[field.length];
        for (int i = 0; i < row.length; i++)
            row[i] = field[rowId][i];
        return row;
    }

    /**
     * Extracts a Field Row from the field(row/column)
     */
    public static SingleFieldState[] extractCol(SingleFieldState[][] field, int colId) {
        SingleFieldState[] col = new SingleFieldState[field.length];
        for (int i = 0; i < col.length; i++)
            col[i] = field[i][colId];
        return col;
    }

    public static SingleFieldState[] exttactMainDiagonal(SingleFieldState[][] field) {
        SingleFieldState[] diagonal = new SingleFieldState[field.length];
        for (int i = 0; i < diagonal.length; i++)
            diagonal[i] = field[i][i];
        return diagonal;
    }

    public static SingleFieldState[] extractAntiDiagnal(SingleFieldState[][] field) {
        SingleFieldState[] diagonal = new SingleFieldState[field.length];
        int max = diagonal.length - 1;
        for (int i = 0; i < diagonal.length; i++)
            diagonal[i] = field[max - i][i];
        return diagonal;

    }

    public static int calculateFieldValue(SingleFieldState[][] field, SingleFieldState type) {
        var lines = getAllLines(field);
        return lines.stream().mapToInt(x -> {
            return calculateLineValue(x, type);
        }).sum();
    }

    private static int calculateLineValue(SingleFieldState[] line, SingleFieldState type) {
        int value = 0;
        int max = line.length;
        for (int i = 0; i < max; i++)
            if (line[i] != SingleFieldState.Empty)
                value += line[i] == type ? 1 : -1;
        if (max == value)
            value = max * max;
        else if (-max + 1 == value)
            value += value;
        return value;
    }

    public static boolean gameOver(SingleFieldState[][] field) {
        var a = Arrays.stream(field).flatMap(Arrays::stream).collect(Collectors.toList());
        return !a.contains(SingleFieldState.Empty);
    }

    public static SingleFieldState getWinner(SingleFieldState[][] field) {
        if (!gameOver(field))
            return SingleFieldState.Empty;
        var lines = getAllLines(field);
        var v = lines.stream().filter(x -> {
            return Arrays.stream(x).distinct().count() == 1;
        }).collect(Collectors.toList());
        SingleFieldState winner = SingleFieldState.Empty;
        if (v.size() == 1)
            winner = v.get(0)[0];
        return winner;
    }

    private static List<SingleFieldState[]> getAllLines(SingleFieldState[][] field) {
        List<SingleFieldState[]> lines = new ArrayList<>();
        for (int i = 0; i < field.length; i++) {
            lines.add(extractCol(field, i));
            lines.add(extractRow(field, i));
        }
        lines.add(extractAntiDiagnal(field));
        lines.add(exttactMainDiagonal(field));
        return lines;
    }
}