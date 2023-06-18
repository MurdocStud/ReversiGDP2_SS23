import java.util.Scanner;

public class OthelloGame {
    private char[][] board;
    private char currentPlayer;

    public OthelloGame() {
        board = new char[8][8];
        currentPlayer = 'B';
        initializeBoard();
    }

    public void play() {
        boolean gameFinished = false;

        while (!gameFinished) {
            printBoard();

            if (isGameOver()) {
                gameFinished = true;
                continue;
            }

            if (currentPlayer == 'B') {
                if (!makeMove()) {
                    System.out.println("Ungültiger Zug! Versuche es erneut.");
                    continue;
                }
            } else {
                makeAIMove();
            }

            currentPlayer = (currentPlayer == 'B') ? 'W' : 'B';
        }

        printBoard();
        displayResults();
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = '-';
            }
        }

        board[3][3] = 'W';
        board[3][4] = 'B';
        board[4][3] = 'B';
        board[4][4] = 'W';
    }

    private void printBoard() {
        System.out.println("  0 1 2 3 4 5 6 7");

        for (int i = 0; i < 8; i++) {
            System.out.print(i + " ");

            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }

            System.out.println();
        }
    }

    private boolean isValidMove(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8 || board[row][col] != '-') {
            return false;
        }

        char opponent = (currentPlayer == 'B') ? 'W' : 'B';

        // Check in all eight directions for a valid move
        int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int r = row + rowDirections[i];
            int c = col + colDirections[i];
            boolean foundOpponent = false;

            while (r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c] == opponent) {
                r += rowDirections[i];
                c += colDirections[i];
                foundOpponent = true;
            }

            if (r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c] == currentPlayer && foundOpponent) {
                return true;
            }
        }

        return false;
    }

    private boolean makeMove() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Spieler " + currentPlayer + ", gib deinen Zug ein (Reihe Spalte): ");
        int row = scanner.nextInt();
        int col = scanner.nextInt();

        if (!isValidMove(row, col)) {
            return false;
        }

        board[row][col] = currentPlayer;
        flipOpponentPieces(row, col);
        return true;
    }

    private void flipOpponentPieces(int row, int col) {
        char opponent = (currentPlayer == 'B') ? 'W' : 'B';

        int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int r = row + rowDirections[i];
            int c = col + colDirections[i];
            boolean foundOpponent = false;
            boolean foundCurrentPlayer = false;
            int piecesFlipped = 0;

            while (r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c] == opponent) {
                r += rowDirections[i];
                c += colDirections[i];
                foundOpponent = true;
                piecesFlipped++;
            }

            if (r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c] == currentPlayer && foundOpponent) {
                foundCurrentPlayer = true;
            }

            if (foundCurrentPlayer) {
                r = row + rowDirections[i];
                c = col + colDirections[i];

                while (piecesFlipped > 0) {
                    board[r][c] = currentPlayer;
                    r += rowDirections[i];
                    c += colDirections[i];
                    piecesFlipped--;
                }
            }
        }
    }

    private void makeAIMove() {
        // Einfache KI: Wählt das erste gültige Feld
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j)) {
                    board[i][j] = currentPlayer;
                    flipOpponentPieces(i, j);
                    return;
                }
            }
        }
    }

    private boolean isGameOver() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    private void displayResults() {
        int blackCount = 0;
        int whiteCount = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 'B') {
                    blackCount++;
                } else if (board[i][j] == 'W') {
                    whiteCount++;
                }
            }
        }

        System.out.println("Spiel beendet.");
        System.out.println("Schwarz: " + blackCount);
        System.out.println("Weiß: " + whiteCount);

        if (blackCount > whiteCount) {
            System.out.println("Schwarz gewinnt!");
        } else if (whiteCount > blackCount) {
            System.out.println("Weiß gewinnt!");
        } else {
            System.out.println("Unentschieden!");
        }
    }

    public static void main(String[] args) {
        OthelloGame game = new OthelloGame();
        game.play();
    }
}
