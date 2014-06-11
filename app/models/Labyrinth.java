package models;

import controllers.routes;
import controllers.utils.Maze;
import play.Logger;
import play.db.ebean.Model;
import play.i18n.Messages;

import javax.persistence.*;
import java.util.List;
import java.util.Random;

/**
 * Created by OM on 08.06.2014.
 */

@Entity
public class Labyrinth extends Model {
    @Id
    public Long id;

    public String title;

    @OneToOne
    public LabyrinthCell currentCell;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "labyrinth")
    public List<LabyrinthCell> cells;

    public static final int N = 10;
    public static Model.Finder<Long, Labyrinth> find = new Model.Finder<Long, Labyrinth>(Long.class, Labyrinth.class);

    public static Labyrinth createLabyrinth() {
        Labyrinth labyrinth = new Labyrinth();
        labyrinth.title = "Test labyrinth";
        labyrinth.save();

        // generate maze
        new Maze(N, labyrinth);

        // set current cell
        List<LabyrinthCell> labyrinthCells = labyrinth.cells;
        LabyrinthCell labyrinthCell = labyrinthCells.get(new Random().nextInt(labyrinthCells.size()));
        labyrinthCell.visited = true;
        labyrinthCell.save();

        labyrinth.currentCell = labyrinthCell;
        labyrinth.save();

        return labyrinth;
    }

    public static void deleteLabyrinth(Labyrinth labyrinth) {

        //Labyrinth labyrinth = Labyrinth.find.byId(labyrinthId);
        labyrinth.currentCell = null;
        labyrinth.save();
        /*
        List<LabyrinthCell> labyrinthCells = LabyrinthCell.findByLabyrinth(labyrinth);
        for (LabyrinthCell labyrinthCell : labyrinthCells) {
            labyrinthCell.delete();
        }
        */
        labyrinth.delete();

    }


    @Override
    public String toString() {
        return "Labyrinth{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", currentCell=" + (currentCell == null ? "no" : currentCell.id) +
                ", cells=" + cells.size() +
                '}';
    }

    public String getMap() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div class=\"table\">");

        for (int row = 0; row < N; row++) {
            stringBuilder.append("<div class=\"row\">");
            for (int col = 0; col < N; col++) {
                LabyrinthCell labyrinthCell = LabyrinthCell.findByLabyrinthAndCoordinates(this, row, col);
                if (labyrinthCell == null) {
                    Logger.error("Cell with labyrinth=" + this.id + ", row=" + row + ", col=" + col + " not found");
                    continue;
                }

                stringBuilder.append("<div class=\"cell\" style=\"");
                if (labyrinthCell.visited == false) {
                    stringBuilder.append("background-color:#CCCCCC;");
                } else {
                    if (labyrinthCell.equals(currentCell)) {
                        stringBuilder.append("background-color:#57DE91;");
                    }
                    if (labyrinthCell.northWall) {
                        stringBuilder.append("border-top-color:black;");
                    }
                    if (labyrinthCell.eastWall) {
                        stringBuilder.append("border-right-color:black;");
                    }
                    if (labyrinthCell.southWall) {
                        stringBuilder.append("border-bottom-color:black;");
                    }
                    if (labyrinthCell.westWall) {
                        stringBuilder.append("border-left-color:black;");
                    }
                }

                stringBuilder.append("\">");
                stringBuilder.append(labyrinthCell.id);
                stringBuilder.append("</div>");
            }
            stringBuilder.append("</div>");
        }

        stringBuilder.append("</div>");
        return stringBuilder.toString();
    }

    public String getActionsInCurrentCell() {
        StringBuilder stringBuilder = new StringBuilder("<p>Actions:<br>");

        if (!currentCell.northWall) {
            stringBuilder.append(String.format("<a href=\"%s\">%s</a><br>", routes.Dashboard.goNorth(),
                    Messages.get("labyrynth.goNorth")));
        }
        if (!currentCell.eastWall) {
            stringBuilder.append(String.format("<a href=\"%s\">%s</a><br>", routes.Dashboard.goEast(),
                    Messages.get("labyrynth.goEast")));
        }
        if (!currentCell.southWall) {
            stringBuilder.append(String.format("<a href=\"%s\">%s</a><br>", routes.Dashboard.goSouth(),
                    Messages.get("labyrynth.goSouth")));
        }
        if (!currentCell.westWall) {
            stringBuilder.append(String.format("<a href=\"%s\">%s</a><br>", routes.Dashboard.goWest(),
                    Messages.get("labyrynth.goWest")));
        }
        stringBuilder.append("</p>");
        return stringBuilder.toString();
    }
}
