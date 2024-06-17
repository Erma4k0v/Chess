package com.example.chess;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        int rowDiff = Math.abs(startRow - endRow);
        int colDiff = Math.abs(startCol - endCol);
        return ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) && (board[endRow][endCol] == null || board[endRow][endCol].isWhite() != isWhite());
    }
}
