package com.example.lr4;

import javafx.scene.input.MouseEvent;

import javafx.scene.control.Label;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import javafx.scene.paint.Color;

import javafx.scene.shape.Circle;

public class CheckersController {
    private boolean isFirstPlayerTurn = true;
    private Circle selectedChecker = null;
    private boolean isCheckerSwitchBlocked = false;
    private Label playerTurnLabel = null;
    private int selectedRow;
    private int selectedCol;

    private final CheckersRules rules;
    private final Circle[][] board;


    public CheckersController() {
        rules = new CheckersRules();
        board = new Circle[8][8];
    }

    public void updateBoard(int row, int col, Circle checker) {
        board[row][col] = checker;
    }

    public void setPlayerTurnLabel(Label playerTurnLabel) {
        this.playerTurnLabel = playerTurnLabel;
    }

    private void updatePlayerTurn() {
        if (playerTurnLabel == null) return;
        isFirstPlayerTurn = !isFirstPlayerTurn;

        if (isFirstPlayerTurn) {
            this.playerTurnLabel.setStyle("-fx-text-fill: red;");
            this.playerTurnLabel.setText("Ход красных");

            return;
        }

        this.playerTurnLabel.setStyle("-fx-text-fill: blue;");
        this.playerTurnLabel.setText("Ход синих");
    }

    // Обработчик кликов по черной клетке
    public void handleBlackTileClick(MouseEvent event, StackPane stackPane, int row, int col) {
        if (selectedChecker != null) {
            if (rules.isValidCapture(selectedRow, selectedCol, row, col, isFirstPlayerTurn, board)) {
                int priviesRow = selectedRow;
                int priviesCol = selectedCol;

                moveChecker(stackPane, row, col);
                removeCapturedChecker(priviesRow, priviesCol);

                if (rules.isAroundCapture(selectedRow, selectedCol, isFirstPlayerTurn, board)) {
                    this.isCheckerSwitchBlocked = true;
                }
                else {
                    this.isCheckerSwitchBlocked = false;
                    updatePlayerTurn();
                }

                if (this.checkEnd()) {
                    System.out.println("Идёт");
                    this.playerTurnLabel.setText("Игра окончена");
                    this.playerTurnLabel.setStyle("-fx-text-fill: gray;");
                }

                return;
            }
            if (rules.isValidMove(selectedRow, selectedCol, row, col, isFirstPlayerTurn)) {
                moveChecker(stackPane, row, col);

                updatePlayerTurn();

                return;
            }

            System.out.println("Неверный ход");
        }
        System.out.println("Нажатие на клетку " + row + " " + col);
    }

    private void moveChecker(StackPane targetStackPane, int toRow, int toCol) {
        if (selectedChecker != null) {
            StackPane oldStackPane = (StackPane) selectedChecker.getParent();
            oldStackPane.getChildren().remove(selectedChecker);

            targetStackPane.getChildren().add(selectedChecker);

            GridPane.setRowIndex(selectedChecker, toRow);
            GridPane.setColumnIndex(selectedChecker, toCol);

            board[toRow][toCol] = selectedChecker;
            board[selectedRow][selectedCol] = null;

            System.out.println("Положение шашки после перемещения в grid pane; строка " + GridPane.getRowIndex(selectedChecker)
                    + " столбец " + GridPane.getColumnIndex(selectedChecker));

            selectedRow = toRow;
            selectedCol = toCol;

            System.out.println("Шашка перемещена: новая строка " + toRow + ", новый столбец " + toCol);
        }
    }

    private void removeCapturedChecker(int row, int col) {
        int capturedRow = (row + selectedRow) / 2;
        int capturedCol = (col + selectedCol) / 2;

        StackPane capturedStackPane = (StackPane) board[capturedRow][capturedCol].getParent();
        capturedStackPane.getChildren().remove(board[capturedRow][capturedCol]);
        board[capturedRow][capturedCol] = null;

        System.out.println("Срублена шашка на позиции: строка " + capturedRow + ", столбец " + capturedCol);

    }

    public void handleCheckerClick(MouseEvent event, Circle checker, Color color) {
        if (this.isCheckerSwitchBlocked) return;

        Integer currentRow = GridPane.getRowIndex(checker);
        Integer currentCol = GridPane.getColumnIndex(checker);
        if ((isFirstPlayerTurn && color.equals(Color.RED)) || (!isFirstPlayerTurn && color.equals(Color.BLUE))) {
            selectedChecker = checker;
            selectedRow = currentRow;
            selectedCol = currentCol;
            System.out.println("Шашка выбрана: строка " + currentRow + ", столбец " + currentCol);
        } else {
            System.out.println("Не ваша очередь ходить");
            selectedChecker = null;
        }
        event.consume();
    }

    private boolean checkEnd() {
        Color firstColor = null;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (this.board[i][j] == null) continue;

                if (firstColor == null) {
                    firstColor = (Color) this.board[i][j].getFill();
                    continue;
                }

                if (this.board[i][j].getFill() != null && this.board[i][j].getFill() != firstColor) {
                    return false;
                }
            }
        }

        System.out.println("Игра окончена");
        return true;
    }
}
