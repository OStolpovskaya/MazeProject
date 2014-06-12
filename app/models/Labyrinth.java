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

    public int size;

    @ManyToOne
    public World world;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "labyrinth")
    public List<LabyrinthCell> cells;

    public static Model.Finder<Long, Labyrinth> find = new Model.Finder<Long, Labyrinth>(Long.class, Labyrinth.class);

    public static Labyrinth createLabyrinth(World world, String title, int size) {
        Labyrinth labyrinth = new Labyrinth();
        labyrinth.title = title;
        labyrinth.world = world;
        labyrinth.size = size;
        labyrinth.save();

        // generate maze
        new Maze(size, labyrinth);
        labyrinth.save();

        // addMapObjects for passes
        for (LabyrinthCell cell : labyrinth.cells) {
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
        labyrinth.delete();
    }


    @Override
    public String toString() {
        return "Labyrinth{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cells=" + cells.size() +
                '}';
    }

    public String getMap() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div class=\"table\">");

        for (int row = 0; row < size; row++) {
            stringBuilder.append("<div class=\"row\">");
            for (int col = 0; col < size; col++) {
                LabyrinthCell labyrinthCell = LabyrinthCell.findByLabyrinthAndCoordinates(this, row, col);
                if (labyrinthCell == null) {
                    Logger.error("Cell with labyrinth=" + this.id + ", row=" + row + ", col=" + col + " not found");
                    continue;
                }

                stringBuilder.append("<div class=\"cell\" style=\"");
                //if (labyrinthCell.visited == false) {     // TODO: uncomment this
                if (1==0) {
                    stringBuilder.append("background-color:#CCCCCC;");
                } else {
                    if (labyrinthCell.equals(world.currentCell)) {
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


    public LabyrinthCell getRandomCell() {
        return cells.get(new Random().nextInt(cells.size()));
    }
}
