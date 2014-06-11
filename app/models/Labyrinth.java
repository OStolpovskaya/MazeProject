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
        labyrinth.save();

        // set current cell
        List<LabyrinthCell> labyrinthCells = labyrinth.cells;
        LabyrinthCell labyrinthCell = labyrinthCells.get(new Random().nextInt(labyrinthCells.size()));
        labyrinthCell.visited = true;
        labyrinthCell.save();

        labyrinth.currentCell = labyrinthCell;
        labyrinth.save();

        // addMapObjects for passes
        for (LabyrinthCell cell : labyrinthCells) {
            if (!cell.northWall) {
                cell.mapObjects.add(MapObject.createNorthPass(cell, LabyrinthCell.findNorthTo(cell).id));
            }
            if (!cell.eastWall) {
                cell.mapObjects.add(MapObject.createEastPass(cell, LabyrinthCell.findEastTo(cell).id));
            }
            if (!cell.southWall) {
                cell.mapObjects.add(MapObject.createSouthPass(cell, LabyrinthCell.findSouthTo(cell).id));
            }
            if (!cell.westWall) {
                cell.mapObjects.add(MapObject.createWestPass(cell,LabyrinthCell.findWestTo(cell).id));
            }
            cell.save();
        }

        return labyrinth;
    }

    public static void deleteLabyrinth(Labyrinth labyrinth) {
        labyrinth.currentCell = null;
        labyrinth.save();

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

    public String getMapObjects() {
        StringBuilder stringBuilder = new StringBuilder("<h1>MapObjects:</h1>");
        stringBuilder.append("<p>");
        for (MapObject mapObject : currentCell.mapObjects) {
            stringBuilder.append(Messages.get("mapObject." + mapObject.type.toString())).append(": ");
            stringBuilder.append(String.format("<a href=\"%s\">%s</a><br>", routes.Dashboard.executeAction(mapObject.id),
                    Messages.get("action." + mapObject.type.toString()))).append("<br>");
            stringBuilder.append(mapObject).append("<br>");
        }
        stringBuilder.append("</p>");
        return stringBuilder.toString();
    }
}
