import java.util.Scanner;

public class OthelloGameTwo {
    private char[][] board;
    private char currentPlayer;
    private boolean singlePlayerMode;
    private int difficulty;

    public OthelloGameTwo() {
        board = new char[8][8];
        currentPlayer = 'B';
        singlePlayerMode = true;
        difficulty = 1;
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
                if (singlePlayerMode) {
                    if (!makeMove()) {
                        System.out.println("Ungültiger Zug! Versuche es erneut.");
                        continue;
                    }
                } else {
                    if (!makeMultiplayerMove()) {
                        System.out.println("Ungültiger Zug! Versuche es erneut.");
                        continue;
                    }
                }
            } else {
                if (singlePlayerMode) {
                    makeAIMove();
                } else {
                    if (!makeMultiplayerMove()) {
                        System.out.println("Ungültiger Zug! Versuche es erneut.");
                        continue;
                    }
                }
            }

            currentPlayer = (currentPlayer == 'B') ? 'W' : 'B';
        }

        printBoard();
        displayResults();
        initializeBoard(); // Reset the board for a new game
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

    private boolean makeMultiplayerMove() {
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
        if (singlePlayerMode) {
            // Single-player mode, AI makes a move
            if (difficulty == 1) {
                makeRandomAIMove();
            } else if (difficulty == 2) {
                makeGreedyAIMove();
            }
        } else {
            // Multiplayer mode, do nothing (let the human player make a move)
            System.out.println("Waiting for the other player's move...");
        }
    }

    private void makeRandomAIMove() {
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

    private void makeGreedyAIMove() {
        // Chooses the move that flips the most opponent pieces
        int maxPiecesFlipped = 0;
        int bestRow = -1;
        int bestCol = -1;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j)) {
                    int piecesFlipped = getFlippedPiecesCount(i, j);

                    if (piecesFlipped > maxPiecesFlipped) {
                        maxPiecesFlipped = piecesFlipped;
                        bestRow = i;
                        bestCol = j;
                    }
                }
            }
        }

        if (bestRow != -1 && bestCol != -1) {
            board[bestRow][bestCol] = currentPlayer;
            flipOpponentPieces(bestRow, bestCol);
        }
    }

    private int getFlippedPiecesCount(int row, int col) {
        char opponent = (currentPlayer == 'B') ? 'W' : 'B';

        int[] rowDirections = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colDirections = {-1, 0, 1, -1, 1, -1, 0, 1};

        int piecesFlipped = 0;

        for (int i = 0; i < 8; i++) {
            int r = row + rowDirections[i];
            int c = col + colDirections[i];
            boolean foundOpponent = false;

            while (r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c] == opponent) {
                r += rowDirections[i];
                c += colDirections[i];
                foundOpponent = true;
                piecesFlipped++;
            }

            if (r >= 0 && r < 8 && c >= 0 && c < 8 && board[r][c] == currentPlayer && foundOpponent) {
                break;
            } else {
                piecesFlipped = 0;
            }
        }

        return piecesFlipped;
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
        } else if (blackCount < whiteCount) {
            System.out.println("Weiß gewinnt!");
        } else {
            System.out.println("Unentschieden!");
        }
    }

    private void showMenu() {
        System.out.println("----- Othello-Spiel -----");
        System.out.println("1. Einzelspielermodus");
        System.out.println("2. Mehrspielermodus");
        System.out.println("3. Schwierigkeitsstufe ändern");
        System.out.println("4. Spiel beenden");
        System.out.print("Wähle eine Option: ");
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            showMenu();
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    singlePlayerMode = true;
                    play();
                    break;
                case 2:
                    singlePlayerMode = false;
                    play();
                    break;
                case 3:
                    changeDifficulty();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Ungültige Option. Bitte versuche es erneut.");
                    break;
            }
        }
    }

    private void changeDifficulty() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("----- Schwierigkeitsstufe ändern -----");
        System.out.println("1. Einfach");
        System.out.println("2. Mittel");
        System.out.println("3. Schwer");
        System.out.print("Wähle eine Schwierigkeitsstufe: ");
        int option = scanner.nextInt();

        if (option >= 1 && option <= 3) {
            difficulty = option;
            System.out.println("Schwierigkeitsstufe wurde geändert.");
        } else {
            System.out.println("Ungültige Option. Schwierigkeitsstufe wurde nicht geändert.");
        }
    }

    public static void main(String[] args) {
        OthelloGameTwo game = new OthelloGameTwo();
        game.start();
    }
}
