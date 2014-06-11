package models;

import play.db.ebean.Model;
import scala.annotation.meta.param;

import javax.persistence.*;

/**
 * Created by OM on 12.06.2014.
 */
@Entity
public class MapObject extends Model {
    @Id
    public long id;

    @ManyToOne
    public LabyrinthCell labyrinthCell;

    @Enumerated(EnumType.STRING)
    public MapObjectType type;

    public String param;

    public static Model.Finder<Long, MapObject> find = new Model.Finder<Long, MapObject>(Long.class, MapObject.class);

    public MapObject(LabyrinthCell labyrinthCell, MapObjectType type, String param) {
        this.labyrinthCell = labyrinthCell;
        this.type = type;
        this.param = param;
    }

    @Override
    public String toString() {
        return "MapObject{" +
                "id=" + id +
                ", labyrinthCell=" + labyrinthCell.id +
                ", type=" + type +
                ", param='" + param + '\'' +
                '}';
    }

    public static MapObject createPass(LabyrinthCell cell, MapObjectType type, long labyrinthCellId) {
        return new MapObject(cell, type, String.valueOf(labyrinthCellId));
    }

    public static MapObject createNorthPass(LabyrinthCell cell, Long passToCellId) {
        return new MapObject(cell, MapObjectType.NORTH_PASS, String.valueOf(passToCellId));
    }

    public static MapObject createEastPass(LabyrinthCell cell, Long passToCellId) {
        return new MapObject(cell, MapObjectType.EAST_PASS, String.valueOf(passToCellId));
    }
    public static MapObject createSouthPass(LabyrinthCell cell, Long passToCellId) {
        return new MapObject(cell, MapObjectType.SOUTH_PASS, String.valueOf(passToCellId));
    }
    public static MapObject createWestPass(LabyrinthCell cell, Long passToCellId) {
        return new MapObject(cell, MapObjectType.WEST_PASS, String.valueOf(passToCellId));
    }
}
