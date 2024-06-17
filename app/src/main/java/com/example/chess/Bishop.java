package com.example.chess;

public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        int rowDiff = Math.abs(startRow - endRow);
        int colDiff = Math.abs(startCol - endCol);
        if (rowDiff == colDiff) {
            int rowStep = (endRow - startRow) / rowDiff;
            int colStep = (endCol - startCol) / colDiff;
            for (int i = 1; i < rowDiff; i++) {
                if (board[startRow + i * rowStep][startCol + i * colStep] != null) {
                    return false;
                }
            }
            return board[endRow][endCol] == null || board[endRow][endCol].isWhite() != isWhite();
        }
        return false;
    }
}
