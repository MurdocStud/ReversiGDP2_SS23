package com.example.othello1852;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    private HelloController controller;
    private GridPane boardPane;
    private Label currentPlayerLabel;
    private Label blackScoreLabel;
    private Label whiteScoreLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root);
        root.getChildren().addAll(createMenuPane(scene), createBoardPane(scene), createScorePane());
        primaryStage.setTitle("Reversi Dayakli, Biermann GDP2 2023");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private VBox createMenuPane(Scene scene) {
        VBox menuPane = new VBox();
        menuPane.setSpacing(10);

        Label modeLabel = new Label("Game Mode");
        Button singlePlayerButton = new Button("Single Player");
        Button multiPlayerButton = new Button("Multiplayer");

        singlePlayerButton.setOnAction(event -> {
            controller = new HelloController(Boolean.TRUE);
            updateBoard(scene);
        });

        multiPlayerButton.setOnAction(event -> {
            controller = new HelloController(Boolean.FALSE);
            updateBoard(scene);
        });

        menuPane.getChildren().addAll(modeLabel, singlePlayerButton, multiPlayerButton);
        return menuPane;
    }

    private GridPane createBoardPane(Scene scene) {
        boardPane = new GridPane();
        boardPane.setAlignment(Pos.CENTER);
        boardPane.setHgap(2);
        boardPane.setVgap(2);
        boardPane.setPadding(new Insets(10));

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Circle circle = new Circle(20);
                circle.setFill(Color.GREEN);
                circle.setStroke(Color.BLACK);

                int row = i;
                int col = j;

                circle.setOnMouseClicked(event -> handleMove(row, col, scene));

                boardPane.add(circle, j, i);
            }
        }

        currentPlayerLabel = new Label("Current Player: ");
        boardPane.add(currentPlayerLabel, 0, 8, 8, 1);

        return boardPane;
    }

    private VBox createScorePane() {
        VBox scorePane = new VBox();
        scorePane.setSpacing(10);

        Label blackLabel = new Label("Black Score:");
        Label whiteLabel = new Label("White Score:");
        blackScoreLabel = new Label("0");
        whiteScoreLabel = new Label("0");

        scorePane.getChildren().addAll(blackLabel, blackScoreLabel, whiteLabel, whiteScoreLabel);
        return scorePane;
    }

    private void handleMove(int row, int col, Scene scene) {
        if (controller != null && controller.isValidMove(row, col)) {
            controller.makeMove(row, col);
            updateBoard(scene);
            if (controller.isSinglePlayer)
            {
                controller.makeBotMove();
                updateBoard(scene);
            }

            if (controller.isGameOver()) {
                showGameOverDialog();
            }
        }
    }

    private void updateBoard(Scene scene) {
        char[][] board = controller.getBoard();
        currentPlayerLabel.setText("Current Player: " + controller.getCurrentPlayer());
        if(currentPlayerLabel.equals('W'))
        {
            scene.setFill(Color.BLACK);
        }
        else
        {
            scene.setFill(Color.WHITE);
        }

        // Reset the board colors
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Circle circle = (Circle) boardPane.getChildren().get(i * 8 + j);
                circle.setFill(Color.GREEN);
            }
        }

        // Update valid move cells with blue dots
        for (int[] move : controller.getPossibleMoves()) {
            int row = move[0];
            int col = move[1];
            Circle circle = (Circle) boardPane.getChildren().get(row * 8 + col);
            circle.setFill(Color.BLUE);
        }

        // Update the board with current player's pieces
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Circle circle = (Circle) boardPane.getChildren().get(i * 8 + j);
                if (board[i][j] == 'B') {
                    circle.setFill(Color.BLACK);
                } else if (board[i][j] == 'W') {
                    circle.setFill(Color.WHITE);
                }
            }
        }

        // Update the score labels
        int blackScore = controller.getScore('B');
        int whiteScore = controller.getScore('W');
        blackScoreLabel.setText(String.valueOf(blackScore));
        whiteScoreLabel.setText(String.valueOf(whiteScore));
    }

    private void showGameOverDialog() {
        int blackScore = controller.getScore('B');
        int whiteScore = controller.getScore('W');
        String message;

        if (blackScore > whiteScore) {
            message = "Black wins!";
        } else if (whiteScore > blackScore) {
            message = "White wins!";
        } else {
            message = "It's a tie!";
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText("Game Over!\n\n" + message);
        alert.showAndWait();
    }
}
