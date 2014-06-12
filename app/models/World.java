package models;

import controllers.routes;
import play.db.ebean.Model;
import play.i18n.Messages;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OM on 12.06.2014.
 */

@Entity
public class World extends Model {

    @Id
    public Long id;

    @OneToOne
    public LabyrinthCell currentCell;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "world")
    public List<Labyrinth> labyrinths;

    public static Model.Finder<Long, World> find = new Model.Finder<Long, World>(Long.class, World.class);

    @Override
    public String toString() {
        return "World{" +
                "id=" + id +
                ", currentCell=" + (currentCell == null ? "no" : currentCell.id) +
                ", labyrinths=" + labyrinths.size() +
                '}';
    }

    public static World createWorld() {
        World world = new World();
        world.labyrinths = new ArrayList<>();
        world.save();

        // generate main labyrinth
        Labyrinth mainLabyrinth = Labyrinth.createLabyrinth(world, "Main labyrinth", 10);
        world.labyrinths.add(mainLabyrinth);
        world.save();

        // set current cell
        List<LabyrinthCell> labyrinthCells = mainLabyrinth.cells;
        LabyrinthCell cellForLabyrinthCurrentCell = mainLabyrinth.getRandomCell();
        cellForLabyrinthCurrentCell.visited = true;
        cellForLabyrinthCurrentCell.save();

        world.currentCell = cellForLabyrinthCurrentCell;
        world.save();

        // set cave
        Labyrinth caveLabyrinth = Labyrinth.createLabyrinth(world, "Cave labyrinth", 3);
        world.labyrinths.add(caveLabyrinth);
        world.save();

        LabyrinthCell cellWithEntranceToCave = cellForLabyrinthCurrentCell;//mainLabyrinth.getRandomCell(); // TODO: make random
        LabyrinthCell cellWithExitFromCave = caveLabyrinth.getRandomCell();

        cellWithEntranceToCave.mapObjects.add(new MapObject(cellWithEntranceToCave, MapObjectType.CAVE_ENTRANCE, cellWithExitFromCave.id.toString()));
        cellWithExitFromCave.mapObjects.add(new MapObject(cellWithExitFromCave, MapObjectType.CAVE_EXIT, cellWithEntranceToCave.id.toString()));

        cellWithEntranceToCave.save();
        cellWithExitFromCave.save();

        // set house
        Labyrinth houseLabyrinth = Labyrinth.createLabyrinth(world, "House labyrinth", 2);
        world.labyrinths.add(houseLabyrinth);
        world.save();

        LabyrinthCell cellWithEntranceToHouse = cellForLabyrinthCurrentCell;//mainLabyrinth.getRandomCell(); // TODO: make random
        LabyrinthCell cellWithExitFromHouse = houseLabyrinth.getRandomCell();

        cellWithEntranceToHouse.mapObjects.add(new MapObject(cellWithEntranceToHouse, MapObjectType.HOUSE_ENTRANCE, cellWithExitFromHouse.id.toString()));
        cellWithExitFromHouse.mapObjects.add(new MapObject(cellWithExitFromHouse, MapObjectType.HOUSE_EXIT, cellWithEntranceToHouse.id.toString()));

        cellWithEntranceToHouse.save();
        cellWithExitFromHouse.save();

        return world;
    }

    public static void deleteWorld(World world) {
        world.currentCell = null;
        world.save();

        world.delete();
    }

    public String getMapObjectsOnCurrentCell() {
        StringBuilder dirPassesSB = new StringBuilder("<h1><div style=\"width:500px;\">Проходы:</div></h1>");
        dirPassesSB.append("<p>");
        dirPassesSB.append("northPass<br>");
        dirPassesSB.append("eastPass<br>");
        dirPassesSB.append("southPass<br>");
        dirPassesSB.append("westPass<br>");
        dirPassesSB.append("</p>");
        String passes = dirPassesSB.toString();

        StringBuilder stringBuilder = new StringBuilder("<h1><div style=\"width:500px;\">MapObjects:</div></h1>");
        stringBuilder.append("<p>");
        List<MapObject> mapObjects = currentCell.mapObjects;
        for (MapObject mapObject : mapObjects) {
            String link = String.format("%s:<a href=\"%s\">%s</a>",
                    Messages.get("mapObject." + mapObject.type.toString()),
                    routes.Dashboard.executeAction(mapObject.id),
                    Messages.get("action." + mapObject.type.toString()));
            if (mapObject.type.equals(MapObjectType.NORTH_PASS)) {
                passes = passes.replaceFirst("northPass",link);
            }else if(mapObject.type.equals(MapObjectType.EAST_PASS)){
                passes = passes.replaceFirst("eastPass",link);
            }else if(mapObject.type.equals(MapObjectType.SOUTH_PASS)){
                passes = passes.replaceFirst("southPass",link);
            }else if(mapObject.type.equals(MapObjectType.WEST_PASS)){
                passes = passes.replaceFirst("westPass",link);
            }else{
                stringBuilder.append(link).append("<br>");
            }
        }
        stringBuilder.append("</p>");

        passes = passes.replaceFirst("northPass","");
        passes = passes.replaceFirst("eastPass","");
        passes = passes.replaceFirst("southPass","");
        passes = passes.replaceFirst("westPass","");

        return passes + stringBuilder.toString();
    }
}
