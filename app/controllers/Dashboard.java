package controllers;

import models.*;
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

    public static Result executeAction(long id){
        User user = User.findByEmail(request().username());
        if (user.inLabyrinth()) {
            Labyrinth labyrinth = user.labyrinth;
            MapObject mapObject = MapObject.find.byId(id);
            // check if this mapObject really belongs to user position (currentCell)
            if (labyrinth.currentCell.equals(mapObject.labyrinthCell)){
                Action action = Action.findByMapObjectType(mapObject.type);
                action.execute(user, mapObject);
            }
        }
        return GO_HOME;
    }

}
