package com.marcoantonioaav.lobogames.board;

public class Board {
    public int[][] matrix;
    public int imageResourceId;

    public Board(int[][] matrix, int imageResourceId) {
        this.matrix = matrix;
        this.imageResourceId = imageResourceId;
    }
}
