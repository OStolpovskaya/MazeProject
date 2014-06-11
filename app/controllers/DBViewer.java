package controllers;

import models.Labyrinth;
import models.LabyrinthCell;
import models.User;
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

        return ok(dbviwer.render(users, labyrinths, labyrinthCells));

    }
}
