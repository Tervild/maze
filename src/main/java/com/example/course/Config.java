package com.example.course;

public class Config {

    public static final int WINDOW_WIDTH = 1420;
    public static final int WINDOW_HEIGHT = 790;

    // Мінімальні розміри лабіринту (щоб можна було згенерувати)
    public static final int MIN_LABYRYNTH_WIDTH = 100;  // 5 клітинок мінімум
    public static final int MIN_LABYRYNTH_HEIGHT = 100; // 5 клітинок мінімум

    // Змінні параметри лабіринту
    public static int LABYRYNTH_WIDTH = WINDOW_WIDTH;
    public static int LABYRYNTH_HEIGHT = WINDOW_HEIGHT - 50;

    public static final int SIZE_SQUARE = 20;

    // Динамічні початкові та кінцеві точки
    public static int START_X = SIZE_SQUARE;
    public static int START_Y = (LABYRYNTH_HEIGHT - SIZE_SQUARE) / 2;
    public static int END_X = LABYRYNTH_WIDTH - 2 * SIZE_SQUARE;
    public static int END_Y = (LABYRYNTH_HEIGHT - SIZE_SQUARE) / 2;

    public static final int GRID_WIDTH = WINDOW_WIDTH / SIZE_SQUARE;
    public static final int GRID_HEIGHT = WINDOW_HEIGHT / SIZE_SQUARE;

    /**
     * Оновлює розміри лабіринту та перераховує стартові/кінцеві точки
     */
    public static void updateLabyrinthSize(int width, int height) {
        LABYRYNTH_WIDTH = width;
        LABYRYNTH_HEIGHT = height;

        // Перераховуємо START та END по центру нового розміру
        START_X = SIZE_SQUARE;
        START_Y = (LABYRYNTH_HEIGHT - SIZE_SQUARE) / 2;
        END_X = LABYRYNTH_WIDTH - 2 * SIZE_SQUARE;
        END_Y = (LABYRYNTH_HEIGHT - SIZE_SQUARE) / 2;
    }
}