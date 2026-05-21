package com.example.course;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MainController {
    @FXML
    public TextField rntime1;
    @FXML
    public TextField rntime2;
    @FXML
    public Button cleartime;
    @FXML
    public Button path;
    @FXML
    public Button build;
    @FXML
    public TextField widthField;
    @FXML
    public TextField heightField;
    @FXML
    public Button buildMax;
    @FXML
    private Button algorithmone;
    @FXML
    private Button algorithmtwo;
    @FXML
    private Button startgame;

    @FXML
    protected void onBuildMax() {
        try {
            // Максимальні розміри - використовуємо ВСЮ доступну площу
            int maxWidth = Config.WINDOW_WIDTH; // 1420 пікселів
            int maxHeight = Config.WINDOW_HEIGHT - 50; // 740 пікселів (віднімаємо тільки панель керування)

            // Обчислюємо кількість клітинок
            int cellsWidth = maxWidth / Config.SIZE_SQUARE;  // 1420 / 20 = 71
            int cellsHeight = maxHeight / Config.SIZE_SQUARE; // 740 / 20 = 37

            // Робимо непарними
            if (cellsWidth % 2 == 0) {
                cellsWidth--;
            }
            if (cellsHeight % 2 == 0) {
                cellsHeight--;
            }

            // Перераховуємо фактичні розміри
            int finalWidth = cellsWidth * Config.SIZE_SQUARE;
            int finalHeight = cellsHeight * Config.SIZE_SQUARE;

            // Оновлюємо конфігурацію
            Config.updateLabyrinthSize(finalWidth, finalHeight);

            // Перебудовуємо лабіринт
            Action.clearScene();

            showInfo("Максимальний розмір",
                    "Побудовано максимальний лабіринт!\n" +
                            "Розмір: " + finalWidth + "×" + finalHeight + " пікселів\n" +
                            "Клітинок: " + cellsWidth + "×" + cellsHeight);

        } catch (Exception e) {
            showError("Помилка побудови", "Не вдалося побудувати максимальний лабіринт: " + e.getMessage());
        }
    }

    @FXML
    protected void onBuild(){
        try {
            // Перевірка чи ініціалізовані поля
            if (widthField == null || heightField == null) {
                showError("Помилка ініціалізації", "Поля введення не ініціалізовані. Перезапустіть програму.");
                return;
            }

            String widthText = widthField.getText().trim();
            String heightText = heightField.getText().trim();

            if (widthText.isEmpty() || heightText.isEmpty()) {
                showError("Помилка введення", "Будь ласка, введіть обидва значення M та N!");
                return;
            }

            int m = Integer.parseInt(widthText);
            int n = Integer.parseInt(heightText);

            if (m <= 0 || n <= 0) {
                showError("Помилка введення", "Значення M та N повинні бути більше нуля!");
                return;
            }

            if (m > 1000 || n > 1000) {
                showError("Помилка введення", "Значення M та N занадто великі! Максимум: 1000");
                return;
            }

            // Обчислюємо максимальні розміри лабіринту (від максимуму, а не від поточного)
            int maxWidth = Config.WINDOW_WIDTH; // 1420
            int maxHeight = Config.WINDOW_HEIGHT - 50; // 740

            // Обчислюємо розміри на основі пропорцій M:N
            double ratio = (double) m / n;

            int newWidth, newHeight;

            // Підбираємо розміри так, щоб не перевищити максимальні
            if (maxWidth / ratio <= maxHeight) {
                // Обмежені по ширині
                newWidth = maxWidth;
                newHeight = (int) (maxWidth / ratio);
            } else {
                // Обмежені по висоті
                newHeight = maxHeight;
                newWidth = (int) (maxHeight * ratio);
            }

            // Округлюємо до кратних SIZE_SQUARE
            newWidth = (newWidth / Config.SIZE_SQUARE) * Config.SIZE_SQUARE;
            newHeight = (newHeight / Config.SIZE_SQUARE) * Config.SIZE_SQUARE;

            // Перевіряємо, щоб кількість клітинок була непарною
            int cellsWidth = newWidth / Config.SIZE_SQUARE;
            int cellsHeight = newHeight / Config.SIZE_SQUARE;

            // Якщо парна кількість - віднімаємо один SIZE_SQUARE
            if (cellsWidth % 2 == 0) {
                newWidth -= Config.SIZE_SQUARE;
            }
            if (cellsHeight % 2 == 0) {
                newHeight -= Config.SIZE_SQUARE;
            }

            // Перевірка мінімальних розмірів
            if (newWidth < Config.MIN_LABYRYNTH_WIDTH || newHeight < Config.MIN_LABYRYNTH_HEIGHT) {
                showError("Помилка розмірів",
                        "Лабіринт занадто малий для генерації!\n" +
                                "Мінімальні розміри: " + Config.MIN_LABYRYNTH_WIDTH + "×" + Config.MIN_LABYRYNTH_HEIGHT + " пікселів\n" +
                                "Ваші розміри: " + newWidth + "×" + newHeight + " пікселів\n" +
                                "Спробуйте інші пропорції або менші значення M та N.");
                return;
            }

            // Оновлюємо конфігурацію
            Config.updateLabyrinthSize(newWidth, newHeight);

            // Перебудовуємо лабіринт
            Action.clearScene();

            showInfo("Успіх", "Лабіринт перебудовано з пропорціями " + m + ":" + n +
                    "\nФактичні розміри: " + newWidth + "×" + newHeight + " пікселів" +
                    "\nКлітинок: " + (newWidth/Config.SIZE_SQUARE) + "×" + (newHeight/Config.SIZE_SQUARE));

        } catch (NumberFormatException e) {
            showError("Помилка формату", "Будь ласка, введіть коректні цілі числа для M та N!");
        } catch (Exception e) {
            showError("Непередбачена помилка", "Сталася помилка: " + e.getMessage());
        }
    }

    @FXML
    protected void onAlgorithmOne() {
        long start = System.nanoTime();
        Action.runAlgorithmOne();
        long end = System.nanoTime();

        long duration = end - start;
        rntime1.setText((duration / 1_000_000.0) + " мс");

    }

    @FXML
    protected void onClear(){
        Action.clearScene();

    }

    @FXML
    protected void onAlgorithmTwo() {
        long start = System.nanoTime();
        Action.runAlgorithmTwo();
        long end = System.nanoTime();
        long duration = end - start;
        rntime2.setText((duration / 1_000_000.0) + " мс");

    }

    @FXML
    protected void onClearRunTime(){
        rntime1.clear();
        rntime2.clear();
    }

    @FXML
    protected void onPath(){
        try {
            Action.Path();
            showInfo("Пошук шляху", "Шлях від входу до виходу знайдено та відмічено жовтим кольором!");
        } catch (Exception e) {
            showError("Помилка пошуку", "Не вдалося знайти шлях: " + e.getMessage() +
                    "\nСпершу згенеруйте лабіринт!");
        }
    }
    @FXML
    protected void onStartGame(){
        Action.startGame();
    }

    // Методи для відображення повідомлень
    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}