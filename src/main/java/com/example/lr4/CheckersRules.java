package com.example.lr4;

import javafx.scene.paint.Color;

import javafx.scene.shape.Circle;

public class CheckersRules {

    // Проверяем, есть ли у шашки возможность рубить
    public boolean canCapture(int fromRow, int fromCol, int toRow, int toCol, boolean isFirstPlayerTurn, Circle[][] board) {
        int opponentRow = (fromRow + toRow) / 2;
        int opponentCol = (fromCol + toCol) / 2;

        // Проверяем, что между клетками (from и to) находится шашка противника
        Circle opponentChecker = board[opponentRow][opponentCol];
        if (opponentChecker != null) {
            Color opponentColor = isFirstPlayerTurn ? Color.BLUE : Color.RED;
            return opponentChecker.getFill().equals(opponentColor);
        }
        return false;
    }

    // Проверка, доступен ли ход (сруб)
    public boolean isValidCapture(int fromRow, int fromCol, int toRow, int toCol, boolean isFirstPlayerTurn, Circle[][] board) {
        if (Math.abs(fromRow - toRow) == 2 && Math.abs(fromCol - toCol) == 2) {
            // Проверка, что клетка для сруба пуста
            if (board[toRow][toCol] == null) {
                return canCapture(fromRow, fromCol, toRow, toCol, isFirstPlayerTurn, board);
            }
        }
        return false;
    }

    public boolean isAroundCapture(int row, int col, boolean isFirstPlayerTurn, Circle[][] board) {
        // Проверяем возможность сруба в четырех направлениях (диагонально)
        // Вверх-влево
        if (row - 2 >= 0 && col - 2 >= 0 && isValidCapture(row, col, row - 2, col - 2, isFirstPlayerTurn, board)) {
            return true;
        }

        // Вверх-вправо
        if (row - 2 >= 0 && col + 2 < board[0].length && isValidCapture(row, col, row - 2, col + 2, isFirstPlayerTurn, board)) {
            return true;
        }

        // Вниз-влево`
        if (row + 2 < board.length && col - 2 >= 0 && isValidCapture(row, col, row + 2, col - 2, isFirstPlayerTurn, board)) {
            return true;
        }

        // Вниз-вправо
        if (row + 2 < board.length && col + 2 < board[0].length && isValidCapture(row, col, row + 2, col + 2, isFirstPlayerTurn, board)) {
            return true;
        }

        return false;
    }

    // Проверяем, может ли игрок выполнить обычный ход (для не рубящих ходов)
    public boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol, boolean isFirstPlayerTurn) {
        if (Math.abs(fromRow - toRow) == 1 && Math.abs(fromCol - toCol) == 1) {
            if (isFirstPlayerTurn && toRow > fromRow) {
                return true;
            } else if (!isFirstPlayerTurn && toRow < fromRow) {
                return true;
            }
        }
        return false;
    }
}

