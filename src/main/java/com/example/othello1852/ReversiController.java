package com.example.othello1852;

import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReversiController {
    private final char[][] board;
    private char currentPlayer;
    public boolean isSinglePlayer;
    private int PassCounter = 0;

    public ReversiController(Boolean IsSinglePlayer) {
        setSinglePlayer(IsSinglePlayer);
        board = new char[8][8];

        // Board wird Initialisiert
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = '-';
            }
        }

        resetBoard();
    }
    //führt einen Zug für den aktuellen Spieler aus. Sie überprüft, ob der Zug gültig ist, aktualisiert das Spielbrett und wechselt den aktuellen Spieler
    public void makeMove(int row, int col) {
        if (isValidMove(row, col)) {
            board[row][col] = currentPlayer;
            flipPieces(row, col);
            currentPlayer = (currentPlayer == 'B') ? 'W' : 'B';
        }
    }
    //führt einen Zug für den Bot aus. Sie wählt einen zufälligen gültigen Zug aus und führt ihn aus
    public void makeBotMove()
    {
        int [] move = getRandomMove();
        System.out.println(move[0]+" "+move[1]);
        makeMove(move[0],move[1]);
    }
    //aktualisiert das Spielbrett, indem sie die Spielsteine umdreht, die durch den aktuellen Zug erfasst werden
    private void flipPieces(int row, int col) {
        PassCounter = 0;
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        for (int[] direction : directions) {
            int i = direction[0];
            int j = direction[1];

            int r = row + i;
            int c = col + j;

            while (r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c] == (currentPlayer == 'B' ? 'W' : 'B')) {
                r += i;
                c += j;
            }

            if (r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c] == currentPlayer) {
                r -= i;
                c -= j;

                while (r != row || c != col) {
                    board[r][c] = currentPlayer;
                    r -= i;
                    c -= j;
                }
            }
        }
    }
    //überprüft, ob ein Zug gültig ist. Ein Zug ist gültig, wenn er innerhalb des Spielbretts liegt und mindestens einen gegnerischen Spielstein einfängt
    public boolean isValidMove(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8 || board[row][col] != '-')
            return false;

        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        for (int[] direction : directions) {
            int i = direction[0];
            int j = direction[1];

            int r = row + i;
            int c = col + j;

            if (r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c] == (currentPlayer == 'B' ? 'W' : 'B')) {
                r += i;
                c += j;

                while (r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c] == (currentPlayer == 'B' ? 'W' : 'B')) {
                    r += i;
                    c += j;
                }

                if (r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c] == currentPlayer)
                    return true;
            }
        }

        return false;
    }
    //überprüft, ob das Spiel vorbei ist. Das Spiel ist vorbei, wenn alle Felder auf dem Spielbrett belegt sind
    public boolean isGameOver() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == '-')
                    return false;
            }
        }
        return true;
    }

    public char[][] getBoard() {
        return board;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }
    //gibt eine Liste von möglichen Zügen für den aktuellen Spieler zurück. Wenn es keine möglichen Züge gibt, überprüft die Methode, ob das Spiel vorbei ist oder ob der aktuelle Spieler passen muss
    public List<int[]> getPossibleMoves() {
        List<int[]> possibleMoves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j)) {
                    possibleMoves.add(new int[]{i, j});
                }
            }
        }
        if (possibleMoves.isEmpty()&&PassCounter == 1)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("Game Over, " + (getScore('B') > getScore('W') ? "B" : "W") + " wins!\nScore: W: " + getScore('W') + ", B: " + getScore('B'));
            alert.showAndWait();
            resetBoard();
        }
        else if(possibleMoves.isEmpty()&&PassCounter == 0)
        {
            PassCounter++;
            currentPlayer = (currentPlayer == 'B') ? 'W' : 'B';
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(getCurrentPlayer()+ "kann keinen Zug machen");
            alert.setContentText(getCurrentPlayer()+" ist jetzt am Zug");
            alert.showAndWait();

        }
        return possibleMoves;
    }
    //gibt einen zufälligen gültigen Zug für den aktuellen Spieler zurück
    public int[] getRandomMove() {
        List<int[]> possibleMoves = getPossibleMoves();
        Random random = new Random();
        if (!possibleMoves.isEmpty()) {
            return possibleMoves.get(random.nextInt(possibleMoves.size()));
        }
        return new int[0];  // Returns an empty array instead of null
    }
    //gibt die Punktzahl eines Spielers zurück. Die Punktzahl eines Spielers ist die Anzahl der Steine, die er auf dem Spielbrett hat
    public int getScore(char player) {
        int score = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == player) {
                    score++;
                }
            }
        }

        return score;
    }
    //setzt das Spielbrett und den aktuellen Spieler zurück. Sie wird aufgerufen, um ein neues Spiel zu starten
    public void resetBoard() {
        currentPlayer = 'W';
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = '-';
            }
        }

        //setzt die Steine in die Anfangsposition
        board[3][3] = 'W';
        board[3][4] = 'B';
        board[4][3] = 'B';
        board[4][4] = 'W';
    }
    //setzt den Spielmodus auf Einzelspieler oder Mehrspieler. Wenn singlePlayer true ist, wird das Spiel im Einzelspielermodus gespielt, bei dem der Spieler gegen den Bot spielt.
    public void setSinglePlayer(boolean singlePlayer)
    {
        this.isSinglePlayer = singlePlayer;
    }
}
