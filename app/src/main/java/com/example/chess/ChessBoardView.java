package com.example.chess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ChessBoardView extends View {

    private static final int BOARD_SIZE = 8;
    private Paint whitePaint;
    private Paint blackPaint;
    private Paint selectedPaint;
    private ChessBoard chessBoard;

    private int selectedRow = -1;
    private int selectedCol = -1;

    private Bitmap whitePawn;
    private Bitmap blackPawn;
    private Bitmap whiteRook;
    private Bitmap blackRook;
    private Bitmap whiteKnight;
    private Bitmap blackKnight;
    private Bitmap whiteBishop;
    private Bitmap blackBishop;
    private Bitmap whiteQueen;
    private Bitmap blackQueen;
    private Bitmap whiteKing;
    private Bitmap blackKing;

    public ChessBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        whitePaint = new Paint();
        whitePaint.setColor(Color.parseColor("#ffffff"));  // HEX цвет для белых клеток

        blackPaint = new Paint();
        blackPaint.setColor(Color.parseColor("#555555"));  // HEX цвет для черных клеток

        selectedPaint = new Paint();
        selectedPaint.setColor(Color.parseColor("#FFFF00"));  // HEX цвет для выделенной клетки

        chessBoard = new ChessBoard(context);

        // Load piece images
        whitePawn = BitmapFactory.decodeResource(getResources(), R.drawable.white_pawn);
        blackPawn = BitmapFactory.decodeResource(getResources(), R.drawable.black_pawn);
        whiteRook = BitmapFactory.decodeResource(getResources(), R.drawable.white_rook);
        blackRook = BitmapFactory.decodeResource(getResources(), R.drawable.black_rook);
        whiteKnight = BitmapFactory.decodeResource(getResources(), R.drawable.white_knight);
        blackKnight = BitmapFactory.decodeResource(getResources(), R.drawable.black_knight);
        whiteBishop = BitmapFactory.decodeResource(getResources(), R.drawable.white_bishop);
        blackBishop = BitmapFactory.decodeResource(getResources(), R.drawable.black_bishop);
        whiteQueen = BitmapFactory.decodeResource(getResources(), R.drawable.white_queen);
        blackQueen = BitmapFactory.decodeResource(getResources(), R.drawable.black_queen);
        whiteKing = BitmapFactory.decodeResource(getResources(), R.drawable.white_king);
        blackKing = BitmapFactory.decodeResource(getResources(), R.drawable.black_king);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int tileSize = Math.min(width, height) / BOARD_SIZE;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Paint paint = (row + col) % 2 == 0 ? whitePaint : blackPaint;
                if (row == selectedRow && col == selectedCol) {
                    canvas.drawRect(col * tileSize, row * tileSize, (col + 1) * tileSize, (row + 1) * tileSize, selectedPaint);
                } else {
                    canvas.drawRect(col * tileSize, row * tileSize, (col + 1) * tileSize, (row + 1) * tileSize, paint);
                }
                Piece piece = chessBoard.getPieceAt(row, col);
                if (piece != null) {
                    drawPiece(canvas, piece, row, col, tileSize);
                }
            }
        }
    }

    private void drawPiece(Canvas canvas, Piece piece, int row, int col, int tileSize) {
        Bitmap bitmap = null;

        if (piece instanceof Pawn) {
            bitmap = piece.isWhite() ? whitePawn : blackPawn;
        } else if (piece instanceof Rook) {
            bitmap = piece.isWhite() ? whiteRook : blackRook;
        } else if (piece instanceof Knight) {
            bitmap = piece.isWhite() ? whiteKnight : blackKnight;
        } else if (piece instanceof Bishop) {
            bitmap = piece.isWhite() ? whiteBishop : blackBishop;
        } else if (piece instanceof Queen) {
            bitmap = piece.isWhite() ? whiteQueen : blackQueen;
        } else if (piece instanceof King) {
            bitmap = piece.isWhite() ? whiteKing : blackKing;
        }

        if (bitmap != null) {
            // Determine the size and position of the piece on the board
            float left = col * tileSize;
            float top = row * tileSize;
            float right = left + tileSize;
            float bottom = top + tileSize;
            canvas.drawBitmap(bitmap, null, new RectF(left, top, right, bottom), null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int tileSize = getWidth() / BOARD_SIZE;
            int col = (int) (event.getX() / tileSize);
            int row = (int) (event.getY() / tileSize);

            // Проверка, попадает ли нажатие в пределы шахматной доски
            if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
                if (selectedRow == -1 && selectedCol == -1) {
                    // Если не выбрана фигура, выбираем её, если она принадлежит текущему игроку
                    if (chessBoard.getPieceAt(row, col) != null && chessBoard.getPieceAt(row, col).isWhite() == chessBoard.isWhiteTurn()) {
                        selectedRow = row;
                        selectedCol = col;
                        invalidate(); // Перерисовываем доску, чтобы показать выбор
                    }
                } else {
                    // Если фигура уже выбрана, пытаемся сделать ход на новую позицию
                    if (chessBoard.isValidMove(selectedRow, selectedCol, row, col)) {
                        chessBoard.movePiece(selectedRow, selectedCol, row, col);
                        selectedRow = -1;
                        selectedCol = -1;
                        invalidate(); // Перерисовываем доску после хода

                        // Проверяем условия шаха после хода
                        if (chessBoard.isKingInCheck(!chessBoard.isWhiteTurn())) {
                            // Обрабатываем ситуацию шаха (по желанию)
                        }
                    } else {
                        // Если ход невозможен, снимаем выбор с фигуры
                        selectedRow = -1;
                        selectedCol = -1;
                        invalidate(); // Перерисовываем доску, чтобы снять выбор
                    }
                }
            }
        }
        return true;  // Обработчик события должен вернуть true, чтобы указать, что событие обработано
    }
}
