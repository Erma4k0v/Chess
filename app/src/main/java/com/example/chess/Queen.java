package com.example.chess;

public class Queen extends Piece {

    public Queen(boolean isWhite) {
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
        } else if (startRow == endRow) {
            int step = (startCol < endCol) ? 1 : -1;
            for (int col = startCol + step; col != endCol; col += step) {
                if (board[startRow][col] != null) {
                    return false;
                }
            }
            return board[endRow][endCol] == null || board[endRow][endCol].isWhite() != isWhite();
        } else if (startCol == endCol) {
            int step = (startRow < endRow) ? 1 : -1;
            for (int row = startRow + step; row != endRow; row += step) {
                if (board[row][startCol] != null) {
                    return false;
                }
            }
            return board[endRow][endCol] == null || board[endRow][endCol].isWhite() != isWhite();
        }

        return false;
    }
}
