package controllers;

import models.*;
import play.Logger;
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
        if (user.inWorld()) {
            return ok(labyrinth.render(user));
        } else {
            return ok(index.render(user));
        }
    }

    public static Result enterWorld() {
        User user = User.findByEmail(request().username());
        if (!user.inWorld()) {
            Logger.debug(String.format("User %s goes to world", user.fullname));
            World world = World.createWorld();
            user.enterWorld(world);
        }
        return GO_HOME;
    }

    public static Result exitWorld() {
        User user = User.findByEmail(request().username());
        if (user.inWorld()) {
            Logger.debug(String.format("User %s goes home", user.fullname));
            World world = user.world;
            user.exitWorld();
            World.deleteWorld(world);
        }
        return GO_HOME;
    }

    public static Result executeAction(long id){
        User user = User.findByEmail(request().username());
        if (user.inWorld()) {
            MapObject mapObject = MapObject.find.byId(id);
            // check if this mapObject really belongs to user position (currentCell)
            if (user.world.currentCell.equals(mapObject.labyrinthCell)){
                Action action = Action.findByMapObjectType(mapObject.type);
                action.execute(user, mapObject);
            }
        }
        return GO_HOME;
    }

}
