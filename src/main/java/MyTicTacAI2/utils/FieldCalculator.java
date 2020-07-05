package MyTicTacAI2.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import MyTicTacAI2.Game.FieldState;

public class FieldCalculator {
    /**
     * Extracts a Field Row from the field(row/column)
     */

    public static FieldState[] extractRow(FieldState[][] field, int rowId) {
        FieldState[] row = new FieldState[field.length];
        for (int i = 0; i < row.length; i++)
            row[i] = field[rowId][i];
        return row;
    }

    /**
     * Extracts a Field Row from the field(row/column)
     */
    public static FieldState[] extractCol(FieldState[][] field, int colId) {
        FieldState[] col = new FieldState[field.length];
        for (int i = 0; i < col.length; i++)
            col[i] = field[i][colId];
        return col;
    }

    public static FieldState[] exttactMainDiagonal(FieldState[][] field) {
        FieldState[] diagonal = new FieldState[field.length];
        for (int i = 0; i < diagonal.length; i++)
            diagonal[i] = field[i][i];
        return diagonal;
    }

    public static FieldState[] extractAntiDiagnal(FieldState[][] field) {
        FieldState[] diagonal = new FieldState[field.length];
        int max = diagonal.length - 1;
        for (int i = 0; i < diagonal.length; i++)
            diagonal[i] = field[max - i][i];
        return diagonal;

    }

    public static int calculateFieldValue(FieldState[][] field, FieldState type) {
        var lines = getAllLines(field);
        return lines.stream().mapToInt(x -> {
            return calculateLineValue(x, type);
        }).sum();
    }

    private static int calculateLineValue(FieldState[] line, FieldState type) {
        int value = 0;
        int max = line.length;
        for (int i = 0; i < max; i++)
            if (line[i] != FieldState.Empty)
                value += line[i] == type ? 1 : -1;
        if (max == value)
            value = max * max;
        else if (-max + 1 == value)
            value += value;
        return value;
    }

    public static boolean gameOver(FieldState[][] field) {
        var fieldList = Arrays.stream(field).flatMap(Arrays::stream).collect(Collectors.toList());
        var winner = getWinner(field);

        return !fieldList.contains(FieldState.Empty) || winner != FieldState.Empty;
    }

    public static FieldState getWinner(FieldState[][] field) {
        var lines = getAllLines(field);
        var v = lines.stream().filter(x -> {
            return Arrays.stream(x).distinct().count() == 1;
        }).collect(Collectors.toList());
        FieldState winner = FieldState.Empty;
        if (v.size() == 1)
            winner = v.get(0)[0];
        else
            winner = FieldState.Empty;
        return winner;
    }

    private static List<FieldState[]> getAllLines(FieldState[][] field) {
        List<FieldState[]> lines = new ArrayList<>();
        for (int i = 0; i < field.length; i++) {
            lines.add(extractCol(field, i));
            lines.add(extractRow(field, i));
        }
        lines.add(extractAntiDiagnal(field));
        lines.add(exttactMainDiagonal(field));
        return lines;
    }
}