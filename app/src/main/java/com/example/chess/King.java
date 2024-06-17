package com.example.chess;

public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        int rowDiff = Math.abs(startRow - endRow);
        int colDiff = Math.abs(startCol - endCol);

        // Проверка на рокировку
        if (rowDiff == 0 && colDiff == 2) {
            if (!hasMoved() && (endCol == 2 || endCol == 6)) {
                int rookCol = endCol == 2 ? 0 : 7;
                Piece rook = board[startRow][rookCol];
                if (rook instanceof Rook && !rook.hasMoved()) {
                    int step = (endCol == 2) ? -1 : 1;
                    for (int col = startCol + step; col != endCol; col += step) {
                        if (board[startRow][col] != null) {
                            return false;
                        }
                    }
                    return true; // Пропускаем проверку шаха здесь, она будет в `movePiece`
                }
            }
        }

        return (rowDiff <= 1 && colDiff <= 1) && (board[endRow][endCol] == null || board[endRow][endCol].isWhite() != isWhite());
    }
}
