package com.example.chess;

import android.content.Context;
import android.widget.Toast;

public class ChessBoard {
    private Piece[][] board;
    private boolean whiteTurn;
    private Context context;

    public ChessBoard(Context context) {
        this.context = context;
        board = new Piece[8][8];
        initializeBoard();
        whiteTurn = true;
    }

    private void initializeBoard() {
        // Инициализация шахматной доски с начальными позициями фигур
        // Белые фигуры
        board[0][0] = new Rook(true);
        board[0][1] = new Knight(true);
        board[0][2] = new Bishop(true);
        board[0][3] = new Queen(true);
        board[0][4] = new King(true);
        board[0][5] = new Bishop(true);
        board[0][6] = new Knight(true);
        board[0][7] = new Rook(true);
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(true);
        }

        // Черные фигуры
        board[7][0] = new Rook(false);
        board[7][1] = new Knight(false);
        board[7][2] = new Bishop(false);
        board[7][3] = new Queen(false);
        board[7][4] = new King(false);
        board[7][5] = new Bishop(false);
        board[7][6] = new Knight(false);
        board[7][7] = new Rook(false);
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(false);
        }
    }

    public Piece getPieceAt(int row, int col) {
        return board[row][col];
    }

    public void setPieceAt(int row, int col, Piece piece) {
        board[row][col] = piece;
    }

    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol) {
        Piece piece = getPieceAt(startRow, startCol);
        if (piece == null || piece.isWhite() != whiteTurn) {
            return false;
        }

        Piece targetPiece = getPieceAt(endRow, endCol);
        if (targetPiece != null && targetPiece.isWhite() == piece.isWhite()) {
            return false;
        }

        if (!piece.isValidMove(startRow, startCol, endRow, endCol, board)) {
            return false;
        }

        if (moveLeavesKingInCheck(startRow, startCol, endRow, endCol, piece.isWhite())) {
            return false;
        }

        return true;
    }

    private boolean moveLeavesKingInCheck(int startRow, int startCol, int endRow, int endCol, boolean isWhite) {
        Piece piece = getPieceAt(startRow, startCol);
        Piece targetPiece = getPieceAt(endRow, endCol);

        setPieceAt(endRow, endCol, piece);
        setPieceAt(startRow, startCol, null);

        boolean inCheck = isKingInCheck(isWhite);

        setPieceAt(startRow, startCol, piece);
        setPieceAt(endRow, endCol, targetPiece);

        return inCheck;
    }

    public void movePiece(int startRow, int startCol, int endRow, int endCol) {
        if (startRow == endRow && startCol == endCol) {
            return;
        }

        if (isValidMove(startRow, startCol, endRow, endCol)) {
            Piece piece = getPieceAt(startRow, startCol);
            Piece targetPiece = getPieceAt(endRow, endCol);

            if (piece instanceof King && Math.abs(startCol - endCol) == 2) {
                int rookCol = endCol == 2 ? 0 : 7;
                int newRookCol = endCol == 2 ? 3 : 5;
                Piece rook = getPieceAt(startRow, rookCol);

                if (rook instanceof Rook && !piece.hasMoved() && !rook.hasMoved()) {
                    setPieceAt(startRow, newRookCol, rook);
                    setPieceAt(startRow, rookCol, null);

                    setPieceAt(endRow, endCol, piece);
                    setPieceAt(startRow, startCol, null);

                    if (isKingInCheck(piece.isWhite())) {
                        setPieceAt(startRow, startCol, piece);
                        setPieceAt(endRow, endCol, null);
                        setPieceAt(startRow, rookCol, rook);
                        setPieceAt(startRow, newRookCol, null);
                        return;
                    }

                    rook.setHasMoved(true);
                    piece.setHasMoved(true);
                }
            } else {
                setPieceAt(endRow, endCol, piece);
                setPieceAt(startRow, startCol, null);
                piece.setHasMoved(true);
            }

            whiteTurn = !whiteTurn;

            checkForCheckOrMate();
        }
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public boolean isCellUnderAttack(int row, int col, boolean isWhite) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = getPieceAt(r, c);
                if (piece != null && piece.isWhite() != isWhite && piece.isValidMove(r, c, row, col, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void checkForCheckOrMate() {
        boolean isCheck = isKingInCheck(whiteTurn);
        boolean isMate = isCheck && !hasValidMoves(whiteTurn);

        if (isCheck) {
            Toast.makeText(context, whiteTurn ? "Белый король под шахом!" : "Черный король под шахом!", Toast.LENGTH_SHORT).show();
        }
        if (isMate) {
            Toast.makeText(context, whiteTurn ? "Белому королю мат!" : "Черному королю мат!", Toast.LENGTH_SHORT).show();
            // Завершить игру
        }
    }

    public boolean isKingInCheck(boolean isWhite) {
        int kingRow = -1, kingCol = -1;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = getPieceAt(r, c);
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    kingRow = r;
                    kingCol = c;
                    break;
                }
            }
        }
        return isCellUnderAttack(kingRow, kingCol, isWhite);
    }

    public boolean hasValidMoves(boolean isWhite) {
        for (int startRow = 0; startRow < 8; startRow++) {
            for (int startCol = 0; startCol < 8; startCol++) {
                Piece piece = getPieceAt(startRow, startCol);
                if (piece != null && piece.isWhite() == isWhite) {
                    for (int endRow = 0; endRow < 8; endRow++) {
                        for (int endCol = 0; endCol < 8; endCol++) {
                            if (isValidMove(startRow, startCol, endRow, endCol)) {
                                Piece savedEndPiece = getPieceAt(endRow, endCol);
                                setPieceAt(endRow, endCol, piece);
                                setPieceAt(startRow, startCol, null);

                                boolean stillInCheck = isKingInCheck(isWhite);

                                setPieceAt(startRow, startCol, piece);
                                setPieceAt(endRow, endCol, savedEndPiece);

                                if (!stillInCheck) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
