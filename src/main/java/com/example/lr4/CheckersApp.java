package com.example.lr4;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.control.Label;
import javafx.scene.control.Button;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import javafx.geometry.Pos;

import javafx.scene.paint.Color;

import javafx.scene.effect.DropShadow;

import javafx.stage.Stage;

public class CheckersApp extends Application {

    private static int TILE_SIZE;
    private static final double SCALE_FACTOR = 0.8;
    private static final int ORIGINAL_TILE_SIZE = 100;
    private static final int ROWS;
    private static final int COLUMNS;

    private CheckersController controller;

    static {
        TILE_SIZE = 100;
        ROWS = 8;
        COLUMNS = 8;
    }

    public CheckersApp() {
        controller = new CheckersController();
        TILE_SIZE = (int) (ORIGINAL_TILE_SIZE * SCALE_FACTOR);
    }

    private void addChecker(StackPane stackPane, int row, int col, Color color) {
        Circle checker = new Circle((double) TILE_SIZE / 2 - 10);  // Радиус шашки чуть меньше половины клетки
        checker.setFill(color);

        DropShadow shadow = new DropShadow();
        checker.radiusProperty().bind(
                stackPane.widthProperty().divide(2).subtract(10)  // Радиус зависит от ширины клетки, с небольшим отступом
        );
        shadow.setColor(Color.GRAY);
        checker.setEffect(shadow);

        checker.setOnMouseClicked(event -> controller.handleCheckerClick(event, checker, color));

        stackPane.getChildren().add(checker);

        GridPane.setRowIndex(checker, row);
        GridPane.setColumnIndex(checker, col);
        controller.updateBoard(row, col, checker);
    }

    private GridPane initializeBoard() {
        GridPane gridPane = new GridPane();

        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gridPane.setMinSize(0, 0);
        gridPane.setPrefSize(TILE_SIZE * COLUMNS, TILE_SIZE * ROWS);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);

                if ((row + col) % 2 == 0) {
                    tile.setFill(Color.WHITE);
                    gridPane.add(tile, col, row);
                } else {
                    tile.setFill(Color.BLACK);

                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().add(tile);
                    stackPane.setAlignment(Pos.CENTER);
                    gridPane.add(stackPane, col, row);

                    int finalRow = row;
                    int finalCol = col;
                    stackPane.setOnMouseClicked(event -> controller.handleBlackTileClick(event, stackPane, finalRow, finalCol));

                    if (row < 3) {
                        addChecker(stackPane, row, col, Color.RED);
                    } else if (row > 4) {
                        addChecker(stackPane, row, col, Color.BLUE);
                    }
                }
            }
        }

        return gridPane;
    }

    private void restartGame(HBox root, Label playerTurnLabel) {
        root.getChildren().clear();

        playerTurnLabel.setText("Ход красных");
        playerTurnLabel.setStyle("-fx-text-fill: red;");

        controller = new CheckersController();
        controller.setPlayerTurnLabel(playerTurnLabel);

        GridPane gridPane = initializeBoard();

        HBox.setHgrow(gridPane, Priority.ALWAYS);
        root.getChildren().add(gridPane);
    }

    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox();
        root.setSpacing(10);

        GridPane gridPane = initializeBoard();

        // Устанавливаем автоматическое расширение ячеек
        HBox.setHgrow(gridPane, Priority.ALWAYS);
        root.getChildren().add(gridPane);

        ToolBar toolBar = new ToolBar();

        Label playerTurnLabel = new Label("Ход красных");
        playerTurnLabel.setStyle("-fx-text-fill: red;");
        controller.setPlayerTurnLabel(playerTurnLabel);

        Button restartButton = new Button("Перезапуск");
        restartButton.setOnMouseClicked(_ -> this.restartGame(root, playerTurnLabel));

        toolBar.getItems().addAll(playerTurnLabel, restartButton);

        // Создаем VBox для размещения ToolBar и доски
        VBox vbox = new VBox();
        vbox.getChildren().addAll(toolBar, root);

        Scene scene = new Scene(vbox, TILE_SIZE * COLUMNS, TILE_SIZE * ROWS + 30);

        gridPane.prefHeightProperty().bind(scene.heightProperty());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Checkers Board");
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
