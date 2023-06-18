package com.example.othello1852;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HelloController {
    private char[][] board;
    private char currentPlayer;

    public HelloController() {
        board = new char[8][8];
        currentPlayer = 'B';

        // Initialize the board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = '-';
            }
        }

        // Initial pieces
        board[3][3] = 'W';
        board[3][4] = 'B';
        board[4][3] = 'B';
        board[4][4] = 'W';
    }

    public void makeMove(int row, int col) {
        if (isValidMove(row, col)) {
            board[row][col] = currentPlayer;
            flipPieces(row, col);
            currentPlayer = (currentPlayer == 'B') ? 'W' : 'B';
        }
    }

    private void flipPieces(int row, int col) {
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

    public List<int[]> getPossibleMoves() {
        List<int[]> possibleMoves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j)) {
                    possibleMoves.add(new int[]{i, j});
                }
            }
        }

        return possibleMoves;
    }

    public int[] getRandomMove() {
        List<int[]> possibleMoves = getPossibleMoves();
        Random random = new Random();
        if (!possibleMoves.isEmpty()) {
            int[] randomMove = possibleMoves.get(random.nextInt(possibleMoves.size()));
            return randomMove;
        }
        return null;
    }
}
