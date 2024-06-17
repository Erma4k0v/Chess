package com.example.chess;

public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        if (startRow != endRow && startCol != endCol) {
            return false;
        }
        if (startRow == endRow) {
            int step = startCol < endCol ? 1 : -1;
            for (int col = startCol + step; col != endCol; col += step) {
                if (board[startRow][col] != null) {
                    return false;
                }
            }
        } else {
            int step = startRow < endRow ? 1 : -1;
            for (int row = startRow + step; row != endRow; row += step) {
                if (board[row][startCol] != null) {
                    return false;
                }
            }
        }
        return board[endRow][endCol] == null || board[endRow][endCol].isWhite() != isWhite();
    }
}
