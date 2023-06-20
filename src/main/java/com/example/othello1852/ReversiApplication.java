package com.example.othello1852;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

//Deklaration der Variablen, die wir in der gesamten Klasse verwenden werden.
//Das beinhaltet das Controller-Objekt, das den Zustand des Spiels verwaltet, die Pane (Panel), die das Spielbrett darstellt,
//sowie Labels für den aktuellen Spieler und die Punktezahlen der schwarzen und weißen Spielers.
public class ReversiApplication extends Application {
    private ReversiController controller;
    private GridPane boardPane;
    private Label currentPlayerLabel;
    private Label blackScoreLabel;
    private Label whiteScoreLabel;

    public static void main(String[] args) {
        launch(args);
    }

    //wird von JavaFX aufgerufen, wenn die Anwendung gestartet wird.
    //Sie ist dafür verantwortlich, das Fenster (Stage) und die Szene (Scene) zu erstellen und alle Elemente darauf zu platzieren.
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        Scene scene = new Scene(root);
        root.getChildren().addAll(createMenuPane(), createBoardPane(), createScorePane());
        primaryStage.setTitle("Reversi Dayakli, Biermann GDP2 2023");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    //erstellt das Menüfeld, das die Bedienelemente für das Spiel enthält, wie Start-, Stopp- und Reset-Buttons.
    private VBox createMenuPane() {
        VBox menuPane = new VBox();
        menuPane.setSpacing(10);

        Label modeLabel = new Label("Game Mode");
        Button singlePlayerButton = new Button("Single Player");
        Button multiPlayerButton = new Button("Multiplayer");

        singlePlayerButton.setOnAction(event -> {
            controller = new ReversiController(Boolean.TRUE);
            updateBoard();
        });

        multiPlayerButton.setOnAction(event -> {
            controller = new ReversiController(Boolean.FALSE);
            updateBoard();
        });

        menuPane.getChildren().addAll(modeLabel, singlePlayerButton, multiPlayerButton);
        return menuPane;
    }
    //erstellt das Spielfeld, das aus einem GridPane besteht.
    //Jedes Feld im GridPane ist entweder leer oder enthält einen Spielstein (dargestellt als Circle).
    private GridPane createBoardPane() {
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

                circle.setOnMouseClicked(event -> handleMove(row, col));

                boardPane.add(circle, j, i);
            }
        }

        currentPlayerLabel = new Label("Current Player: ");
        boardPane.add(currentPlayerLabel, 0, 8, 8, 1);

        return boardPane;
    }
    // Die Methode createScorePane() erstellt das Punktefeld, das die aktuellen Punktezahlen der Spieler anzeigt.
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
    // Die Methode handleMove() wird aufgerufen, wenn ein Spieler (oder der Bot) einen Zug macht.
    // Sie überprüft, ob der Zug gültig ist, führt ihn aus und prüft, ob das Spiel vorbei ist.
    private void handleMove(int row, int col) {
        if (controller != null && controller.isValidMove(row, col)) {
            controller.makeMove(row, col);
            updateBoard();
            if (controller.isSinglePlayer)
            {
                controller.makeBotMove();
                updateBoard();
            }

            if (controller.isGameOver()) {
                showGameOverDialog();
            }
        }
    }
    // Die Methode updateBoard() wird aufgerufen, um das Spielfeld zu aktualisieren, nachdem ein Zug gemacht wurde.
    // Sie aktualisiert die Spielsteine und die Punkteanzeige.
    private void updateBoard() {
        char[][] board = controller.getBoard();
        currentPlayerLabel.setText("Current Player: " + controller.getCurrentPlayer());
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Circle circle = (Circle) boardPane.getChildren().get(i * 8 + j);
                circle.setFill(Color.GREEN);
            }
        }
        for (int[] move : controller.getPossibleMoves()) {
            int row = move[0];
            int col = move[1];
            Circle circle = (Circle) boardPane.getChildren().get(row * 8 + col);
            circle.setFill(Color.BLUE);
        }
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
        int blackScore = controller.getScore('B');
        int whiteScore = controller.getScore('W');
        blackScoreLabel.setText(String.valueOf(blackScore));
        whiteScoreLabel.setText(String.valueOf(whiteScore));
    }
    //wird aufgerufen, wenn das Spiel vorbei ist.
    //zeigt einen Dialog an, der den Gewinner anzeigt und bietet die Möglichkeit, ein neues Spiel zu starten.
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
