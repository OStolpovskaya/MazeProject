package models;

import com.avaje.ebean.Expr;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by OM on 08.06.2014.
 */

@Entity
public class LabyrinthCell extends Model {

    @Id
    public Long id;

    public int row;
    public int col;

    public boolean northWall;
    public boolean eastWall;
    public boolean southWall;
    public boolean westWall;

    public boolean visited = false;

    @ManyToOne
    public Labyrinth labyrinth;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "labyrinthCell")
    public List<MapObject> mapObjects;

    public static Model.Finder<Long, LabyrinthCell> find = new Model.Finder<Long, LabyrinthCell>(Long.class, LabyrinthCell.class);

    public static List<LabyrinthCell> findByLabyrinth(Labyrinth labyrinth) {
        List<LabyrinthCell> labyrinthCells = find.where(Expr.eq("labyrinth", labyrinth)).findList();
        return labyrinthCells;
    }

    public static LabyrinthCell findByLabyrinthAndCoordinates(Labyrinth labyrinth, int row, int col) {
        Map<String, Object> map = new HashMap<>();
        map.put("labyrinth.id", labyrinth.id);
        map.put("row", row);
        map.put("col", col);

        LabyrinthCell labyrinthCell = find.where(Expr.allEq(map)).findUnique();
        return labyrinthCell;
    }

    @Override
    public String toString() {
        return "LabyrinthCell{" +
                "id=" + id +
                ", row=" + row +
                ", col=" + col +
                ", northWall=" + northWall +
                ", eastWall=" + eastWall +
                ", southWall=" + southWall +
                ", westWall=" + westWall +
                ", visited=" + visited +
                ", labyrinth=" + labyrinth.id +
                '}';
    }

    public static LabyrinthCell findNorthTo(LabyrinthCell cell) {
        return findByLabyrinthAndCoordinates(cell.labyrinth,cell.row-1, cell.col);
    }

    public static LabyrinthCell findSouthTo(LabyrinthCell cell) {
        return findByLabyrinthAndCoordinates(cell.labyrinth,cell.row+1, cell.col);
    }

    public static LabyrinthCell findEastTo(LabyrinthCell cell) {
        return findByLabyrinthAndCoordinates(cell.labyrinth,cell.row, cell.col+1);
    }

    public static LabyrinthCell findWestTo(LabyrinthCell cell) {
        return findByLabyrinthAndCoordinates(cell.labyrinth,cell.row, cell.col-1);
    }
}
