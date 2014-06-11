package controllers.utils;

/**
 * Created by OM on 08.06.2014.
 */

import models.Labyrinth;
import models.LabyrinthCell;
import models.MapObject;
import models.MapObjectType;

import java.util.ArrayList;

/**
 * Generates a perfect N-by-N maze using depth-first search with a stack.
 * http://algs4.cs.princeton.edu/41undirected/Maze.java.html
 */
public class Maze {
    private int N;                 // dimension of maze
    private boolean[][] north;     // is there a wall to north of cell i, j
    private boolean[][] east;
    private boolean[][] south;
    private boolean[][] west;
    private boolean[][] visited;
    private double size;
    private boolean done = false;

    public Maze(int N, Labyrinth labyrinth) {
        this.N = N;
        init();
        generate();

        labyrinth.cells = new ArrayList<>();
        for (int x = 1; x < N + 1; x++) {
            for (int y = 1; y < N + 1; y++) {
                LabyrinthCell labyrinthCell = new LabyrinthCell();
                labyrinthCell.labyrinth = labyrinth;
                labyrinthCell.row = N - y;
                labyrinthCell.col = x - 1;
                labyrinthCell.northWall = north[x][y];
                labyrinthCell.eastWall = east[x][y];
                labyrinthCell.southWall = south[x][y];
                labyrinthCell.westWall = west[x][y];
                labyrinthCell.mapObjects = new ArrayList<MapObject>();
                labyrinth.cells.add(labyrinthCell);
            }
        }
        labyrinth.save();
    }

    private void init() {
        // initialize border cells as already visited
        visited = new boolean[N + 2][N + 2];
        for (int x = 0; x < N + 2; x++) visited[x][0] = visited[x][N + 1] = true;
        for (int y = 0; y < N + 2; y++) visited[0][y] = visited[N + 1][y] = true;

        // initialze all walls as present
        north = new boolean[N + 2][N + 2];
        east = new boolean[N + 2][N + 2];
        south = new boolean[N + 2][N + 2];
        west = new boolean[N + 2][N + 2];
        for (int x = 0; x < N + 2; x++)
            for (int y = 0; y < N + 2; y++)
                north[x][y] = east[x][y] = south[x][y] = west[x][y] = true;
    }

    // generate the maze
    private void generate(int x, int y) {
        visited[x][y] = true;

        // while there is an unvisited neighbor
        while (!visited[x][y + 1] || !visited[x + 1][y] || !visited[x][y - 1] || !visited[x - 1][y]) {

            // pick random neighbor (could use Knuth's trick instead)
            while (true) {
                double r = Math.random();
                if (r < 0.25 && !visited[x][y + 1]) {
                    north[x][y] = south[x][y + 1] = false;
                    generate(x, y + 1);
                    break;
                } else if (r >= 0.25 && r < 0.50 && !visited[x + 1][y]) {
                    east[x][y] = west[x + 1][y] = false;
                    generate(x + 1, y);
                    break;
                } else if (r >= 0.5 && r < 0.75 && !visited[x][y - 1]) {
                    south[x][y] = north[x][y - 1] = false;
                    generate(x, y - 1);
                    break;
                } else if (r >= 0.75 && r < 1.00 && !visited[x - 1][y]) {
                    west[x][y] = east[x - 1][y] = false;
                    generate(x - 1, y);
                    break;
                }
            }
        }
    }

    // generate the maze starting from lower left
    private void generate() {
        generate(1, 1);

/*
        // delete some random walls
        for (int i = 0; i < N; i++) {
            int x = (int) (1 + Math.random() * (N-1));
            int y = (int) (1 + Math.random() * (N-1));
            north[x][y] = south[x][y+1] = false;
        }

        // add some random walls
        for (int i = 0; i < 10; i++) {
            int x = (int) (N / 2 + Math.random() * (N / 2));
            int y = (int) (N / 2 + Math.random() * (N / 2));
            east[x][y] = west[x+1][y] = true;
        }
*/

    }

}