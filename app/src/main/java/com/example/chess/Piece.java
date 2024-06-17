// Piece.java
package com.example.chess;

public abstract class Piece {
    private boolean isWhite;
    private boolean hasMoved; // Добавляем поле для отслеживания перемещений

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
        this.hasMoved = false; // По умолчанию фигура не перемещалась
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public abstract boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board);
}
