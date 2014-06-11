package controllers;

import models.Labyrinth;
import models.LabyrinthCell;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.dashboard.index;
import views.html.dashboard.labyrinth;

/**
 * User: yesnault
 * Date: 22/01/12
 */
@Security.Authenticated(Secured.class)
public class Dashboard extends Controller {
    public static Result GO_HOME = redirect(
            routes.Dashboard.index()
    );

    public static Result index() {
        User user = User.findByEmail(request().username());
        if (user.inLabyrinth()) {

            return ok(labyrinth.render(user));
        } else {
            return ok(index.render(user));
        }
    }

    public static Result enterLabyrinth() {
        User user = User.findByEmail(request().username());
        if (!user.inLabyrinth()) {
            Labyrinth labyrinth = Labyrinth.createLabyrinth();
            user.enterLabyrinth(labyrinth);
        }
        return GO_HOME;
    }

    public static Result exitLabyrinth() {
        User user = User.findByEmail(request().username());
        if (user.inLabyrinth()) {
            Labyrinth labyrinth = user.labyrinth;
            user.exitLabyrinth();
            Labyrinth.deleteLabyrinth(labyrinth);
        }
        return GO_HOME;
    }


    public static Result goNorth() {
        User user = User.findByEmail(request().username());
        if (user.inLabyrinth()) {
            Labyrinth labyrinth = user.labyrinth;
            LabyrinthCell currentCell = labyrinth.currentCell;
            if (!currentCell.northWall) {
                LabyrinthCell newCurrentCell = LabyrinthCell.findByLabyrinthAndCoordinates(labyrinth, currentCell.row - 1, currentCell.col);
                newCurrentCell.visited = true;
                newCurrentCell.save();

                labyrinth.currentCell = newCurrentCell;
                labyrinth.save();
            }
        }
        return GO_HOME;
    }

    public static Result goEast() {
        User user = User.findByEmail(request().username());
        if (user.inLabyrinth()) {
            Labyrinth labyrinth = user.labyrinth;
            LabyrinthCell currentCell = labyrinth.currentCell;
            if (!currentCell.eastWall) {
                LabyrinthCell newCurrentCell = LabyrinthCell.findByLabyrinthAndCoordinates(labyrinth, currentCell.row, currentCell.col + 1);
                newCurrentCell.visited = true;
                newCurrentCell.save();

                labyrinth.currentCell = newCurrentCell;
                labyrinth.save();
            }
        }
        return GO_HOME;
    }

    public static Result goSouth() {
        User user = User.findByEmail(request().username());
        if (user.inLabyrinth()) {
            Labyrinth labyrinth = user.labyrinth;
            LabyrinthCell currentCell = labyrinth.currentCell;
            if (!currentCell.southWall) {
                LabyrinthCell newCurrentCell = LabyrinthCell.findByLabyrinthAndCoordinates(labyrinth, currentCell.row + 1, currentCell.col);
                newCurrentCell.visited = true;
                newCurrentCell.save();

                labyrinth.currentCell = newCurrentCell;
                labyrinth.save();
            }
        }
        return GO_HOME;
    }

    public static Result goWest() {
        User user = User.findByEmail(request().username());
        if (user.inLabyrinth()) {
            Labyrinth labyrinth = user.labyrinth;
            LabyrinthCell currentCell = labyrinth.currentCell;
            if (!currentCell.westWall) {
                LabyrinthCell newCurrentCell = LabyrinthCell.findByLabyrinthAndCoordinates(labyrinth, currentCell.row, currentCell.col - 1);
                newCurrentCell.visited = true;
                newCurrentCell.save();

                labyrinth.currentCell = newCurrentCell;
                labyrinth.save();
            }
        }
        return GO_HOME;
    }
}
