package com.example.chess;

public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        int direction = isWhite() ? 1 : -1;
        if (startCol == endCol) {
            if (startRow + direction == endRow && board[endRow][endCol] == null) {
                return true;
            }
            if ((startRow == 1 && isWhite() || startRow == 6 && !isWhite()) && startRow + 2 * direction == endRow && board[startRow + direction][startCol] == null && board[endRow][endCol] == null) {
                return true;
            }
        } else if (Math.abs(startCol - endCol) == 1 && startRow + direction == endRow && board[endRow][endCol] != null && board[endRow][endCol].isWhite() != isWhite()) {
            return true;
        }

        return false;
    }
}
