package com.example.course;

import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;


import java.util.*;


public class Action {
    static Pane root = new Pane();
    private static Map<String, Wall> wallMap = new HashMap<>();
    private static List<Wall> walls = new ArrayList<>();
    private static Group wallsGroup = new Group();
    public static Group players = new Group();
    private static List<Wall> activeWalls = new ArrayList<>();
    private static Player player;
    private static boolean gameActive = false;
    //-----------------------------------------------------------------------------------
    public static void fillSquare(Rectangle square, boolean color){
        if (color){
            square.setFill(Color.rgb(28, 198, 255));
            square.setStroke(null);
        } else {
            square.setFill(Color.rgb(176, 112, 255));
            square.setStroke(Color.BLACK);
            square.setStrokeWidth(1);
            square.setStrokeType(StrokeType.INSIDE);
        }
    }
    //-----------------------------------------------------------------------------------
    public static void setRoot(Pane rt){
        root = rt;
    }
    //-----------------------------------------------------------------------------------
    public static Pane getRoot(){
        return root;
    }
    //-----------------------------------------------------------------------------------
    public static void buildWall() {
        wallMap.clear();
        wallsGroup.getChildren().clear();
        activeWalls.clear();
        walls.clear();

        // Використовуємо поточні розміри з Config
        for (int y = Config.SIZE_SQUARE; y < Config.LABYRYNTH_HEIGHT - Config.SIZE_SQUARE; y += Config.SIZE_SQUARE) {
            for (int x = Config.SIZE_SQUARE; x < Config.LABYRYNTH_WIDTH - Config.SIZE_SQUARE; x += Config.SIZE_SQUARE) {
                if ( x == Config.START_X && y == Config.START_Y){
                    continue;
                } else if (x == Config.END_X && y == Config.END_Y){
                    continue;
                }
                Wall wall = new Wall(x, y);
                wallMap.put(key(x, y), wall);
                walls.add(wall);
                wallsGroup.getChildren().add(wall.getSquare());
            }
        }
        if (!root.getChildren().contains(wallsGroup)) root.getChildren().add(wallsGroup);
    }
    //-----------------------------------------------------------------------------------
    public static void updateWall(int x, int y, int state) {
        Wall wall = getWallAt(x, y);
        if (wall == null) return;
        wall.setActive(state);
        switch (state) {
            case 0 -> { // звичайна стіна
                fillSquare(wall.getSquare(), false);
                activeWalls.remove(wall);
            }
            case 1 -> { // активна стіна
                fillSquare(wall.getSquare(), true);
                if (!activeWalls.contains(wall)) {
                    activeWalls.add(wall);
                }
            }
            case 2 -> { // вже оброблена
                fillSquare(wall.getSquare(), true);
                activeWalls.remove(wall);

            }
        }
    }
    //-----------------------------------------------------------------------------------
    private static Wall getWallAt(int x, int y) {
        return wallMap.get(key(x, y));
    }
    //-----------------------------------------------------------------------------------
    public static void runAlgorithmOne() {

        Wall wall = getWallAt(Config.START_X + Config.SIZE_SQUARE, Config.START_Y);
        if (wall != null && wall.getActive() == 2) {
//            clearScene();
            buildWall();
        }

        // Починаємо з точки біля START
        int startX = Config.START_X + Config.SIZE_SQUARE;
        int startY = Config.START_Y;

        Wall start = getWallAt(startX, startY);
        if (start == null) return;

        // maze = 2, frontier = 1, unused = 0
        updateWall(startX, startY, 2);

        // Додаємо frontier-клітинки
        addFrontier(start);

        while (!activeWalls.isEmpty()) {

            // Беремо випадкову frontier-клітинку
            Wall frontier = activeWalls.get((int) (Math.random() * activeWalls.size()));

            // Знаходимо "maze" сусіда
            Wall mazeNeighbor = getRandomMazeNeighbor(frontier);

            if (mazeNeighbor != null) {

                // Пробиваємо стіну між ними
                int midX = (frontier.getX() + mazeNeighbor.getX()) / 2;
                int midY = (frontier.getY() + mazeNeighbor.getY()) / 2;

                updateWall(midX, midY, 2);
                updateWall(frontier.getX(), frontier.getY(), 2);

                // Додаємо сусідів frontier
                addFrontier(frontier);
            }

            // Видаляємо frontier з activeWalls
            activeWalls.remove(frontier);
        }
    }
    //-----------------------------------------------------------------------------------
    public static void runAlgorithmTwo() {
        Wall wall = getWallAt(Config.START_X + Config.SIZE_SQUARE, Config.START_Y);
        if (wall != null && wall.getActive() == 2) {
//            clearScene();
            buildWall();
        }
        int startX = Config.START_X + Config.SIZE_SQUARE;
        int startY = Config.START_Y;

        // Початкова клітинка
        Wall start = getWallAt(startX, startY);
        if (start == null) return;

        updateWall(startX, startY, 1);

        Stack<Wall> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            Wall current = stack.peek();

            // Знаходимо всіх сусідів на 2 клітинки, які ще не відвідані
            List<Wall> unvisited = new ArrayList<>();

            // Вправо
            Wall right = getWallAt(current.getX() + 2 * Config.SIZE_SQUARE, current.getY());
            if (right != null && right.getActive() == 0) unvisited.add(right);

            // Вліво
            Wall left = getWallAt(current.getX() - 2 * Config.SIZE_SQUARE, current.getY());
            if (left != null && left.getActive() == 0) unvisited.add(left);

            // Вниз
            Wall down = getWallAt(current.getX(), current.getY() + 2 * Config.SIZE_SQUARE);
            if (down != null && down.getActive() == 0) unvisited.add(down);

            // Вгору
            Wall up = getWallAt(current.getX(), current.getY() - 2 * Config.SIZE_SQUARE);
            if (up != null && up.getActive() == 0) unvisited.add(up);

            // Якщо немає сусідів → Backtrack
            if (unvisited.isEmpty()) {
                updateWall(current.getX(), current.getY(), 2); // позначаємо як завершену
                stack.pop();
                continue;
            }

            // Вибираємо випадкового сусіда
            Wall next = unvisited.get((int)(Math.random() * unvisited.size()));

            // Знаходимо "стіну" між current та next
            int midX = (current.getX() + next.getX()) / 2;
            int midY = (current.getY() + next.getY()) / 2;

            Wall midWall = getWallAt(midX, midY);

            // Пробиваємо стіну між клітинками
            if (midWall != null) {
                updateWall(midX, midY, 2);
            }

            // Робимо next активним (visited)
            updateWall(next.getX(), next.getY(), 1);

            // Переходимо до next
            stack.push(next);
        }
    }
    //-----------------------------------------------------------------------------------
    private static void addFrontier(Wall cell) {
        int x = cell.getX();
        int y = cell.getY();

        int[][] dirs = {
                { 2 * Config.SIZE_SQUARE, 0 },
                { -2 * Config.SIZE_SQUARE, 0 },
                { 0, 2 * Config.SIZE_SQUARE },
                { 0, -2 * Config.SIZE_SQUARE }
        };

        for (int[] d : dirs) {
            Wall n = getWallAt(x + d[0], y + d[1]);
            if (n != null && n.getActive() == 0) {
                updateWall(n.getX(), n.getY(), 1); // frontier
            }
        }
    }
    //-----------------------------------------------------------------------------------
    private static Wall getRandomMazeNeighbor(Wall cell) {
        int x = cell.getX();
        int y = cell.getY();

        List<Wall> mazeList = new ArrayList<>();

        int[][] dirs = {
                { 2 * Config.SIZE_SQUARE, 0 },
                { -2 * Config.SIZE_SQUARE, 0 },
                { 0, 2 * Config.SIZE_SQUARE },
                { 0, -2 * Config.SIZE_SQUARE }
        };

        for (int[] d : dirs) {
            Wall n = getWallAt(x + d[0], y + d[1]);
            if (n != null && n.getActive() == 2) { // 2 = maze
                mazeList.add(n);
            }
        }

        if (mazeList.isEmpty()) return null;

        return mazeList.get((int)(Math.random() * mazeList.size()));
    }
    //-----------------------------------------------------------------------------------
    public static void clearScene() {

        walls.clear();
        wallsGroup.getChildren().clear();
        activeWalls.clear();
        buildWall();
    }
    //-----------------------------------------------------------------------------------
    private static String key(int x, int y) {
        return x + "," + y;
    }
    //-----------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------------
    private static List<Wall> getNeighbors(Wall w) {
        List<Wall> list = new ArrayList<>();

        int x = w.getX();
        int y = w.getY();

        int[][] dirs = {
                { Config.SIZE_SQUARE, 0 },
                { -Config.SIZE_SQUARE, 0 },
                { 0, Config.SIZE_SQUARE },
                { 0, -Config.SIZE_SQUARE }
        };

        for (int[] d : dirs) {
            Wall n = getWallAt(x + d[0], y + d[1]);
            if (n != null) {
                list.add(n);
            }
        }

        return list;
    }
    //-----------------------------------------------------------------------------------
    public static void Path() {
        Wall start = getWallAt(Config.START_X + Config.SIZE_SQUARE, Config.START_Y);
        Wall end = getWallAt(Config.END_X - Config.SIZE_SQUARE, Config.END_Y);
        if (start == null || end == null) {
            throw new RuntimeException("Лабіринт ще не побудовано!");
        }
        // Перевіряємо чи є хоч один прохід (клітинка зі станом 2)
        boolean hasPath = false;
        for (Wall wall : walls) {
            if (wall.getActive() == 2) {
                hasPath = true;
                break;
            }
        }
        if (!hasPath) {
            throw new RuntimeException("Лабіринт не згенеровано! Спочатку натисніть 'Алгоритм 1' або 'Алгоритм 2'");
        }
        // BFS черга
        Queue<Wall> queue = new LinkedList<>();
        // Зберігання шляху
        Map<Wall, Wall> parent = new HashMap<>();
        // Відвідані клітини
        Set<Wall> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);
        boolean found = false;
        // BFS цикл
        while (!queue.isEmpty()) {
            Wall current = queue.poll();
            if (current == end) {
                found = true;
                break;
            }
            for (Wall neigh : getNeighbors(current)) {
                if (!visited.contains(neigh) && neigh.getActive() == 2) {
                    visited.add(neigh);
                    parent.put(neigh, current);
                    queue.add(neigh);
                }
            }
        }

        if (!found) {
            throw new RuntimeException("Шлях не знайдено! Можливо, лабіринт пошкоджений.");
        }
        // Відновлюємо шлях назад
        Wall cur = end;
        while (cur != start) {
            Rectangle sq = cur.getSquare();
            sq.setFill(Color.YELLOW); // підсвітка шляху
            cur = parent.get(cur);
        }
        // фарбуємо старт також
        start.getSquare().setFill(Color.YELLOW);
    }
    //-----------------------------------------------------------------------------------
    public static void startGame(){

        int num = (int)(Math.random() * 100);
        if (num % 2  == 0){
            runAlgorithmOne();
        }else{
            runAlgorithmTwo();
        }
        if( player != null  ){
            players.getChildren().clear();
            player = new Player();
            players.getChildren().add(player);
        }else {
            player = new Player();
            players.getChildren().add(player);
        }
        if (!root.getChildren().contains(players)) root.getChildren().add(players);
        System.out.println(players.getChildren().size());
        
    }
    //-----------------------------------------------------------------------------------
    public static void movePlayer(int m){
        if (isPath(m, player)){
            return;
        }

        switch (m){
            case 0 -> player.setY(player.getY() - Config.SIZE_SQUARE);
            case 1 -> player.setX(player.getX() - Config.SIZE_SQUARE);
            case 2 -> player.setY(player.getY() + Config.SIZE_SQUARE);
            case 3 -> player.setX(player.getX() + Config.SIZE_SQUARE);
        }
    }
    //-----------------------------------------------------------------------------------
    public static boolean isPath(int m, Player pl){
        double x = pl.getX();
        double y = pl.getY();
        switch (m){
            case 0 -> y -= Config.SIZE_SQUARE;
            case 1 -> x -= Config.SIZE_SQUARE;
            case 2 -> y += Config.SIZE_SQUARE;
            case 3 -> x += Config.SIZE_SQUARE;
        }

        Wall wall = wallMap.get((int)x + "," + (int)y);
        if (x == Config.START_X && y == Config.START_Y || x == Config.END_X && y == Config.END_Y){
            return false;
        }else if(wall.getActive() == 0){
            return true;
        }else {
            return false;
        }
    }


}