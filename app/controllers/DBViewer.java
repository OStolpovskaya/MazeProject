package controllers;

import models.*;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.dbviwer;

import java.util.List;

/**
 * User: yesnault
 * Date: 22/01/12
 */

public class DBViewer extends Controller {

    public static Result index() {
        List<User> users = User.find.all();
        List<Labyrinth> labyrinths = Labyrinth.find.all();
        List<LabyrinthCell> labyrinthCells = LabyrinthCell.find.all();
        List<MapObject> mapObjects = MapObject.find.all();
        List<Action> actions = Action.find.all();


        return ok(dbviwer.render(users, labyrinths, labyrinthCells, mapObjects, actions));

    }
}
