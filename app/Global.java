import com.avaje.ebean.Ebean;
import models.Action;
import models.MapObjectType;
import models.User;
import play.GlobalSettings;
import play.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by OM on 08.06.2014.
 */
public class Global extends GlobalSettings {
    @Override
    public void onStart(play.Application app) {
        Logger.info("MazeProject has been started");

        // Users
        List<User> users = User.find.all();
        if (users.size() > 0) {
            Ebean.delete(users);
        }
        User user = new User();
        user.email = "o.stolpovskaya@gmail.com";
        user.fullname = "Olga M";
        user.validated = true;
        user.passwordHash = "$2a$10$TLTjdfzdJ3Gl7bnf7estt.PjxLkQHhXJhsIku6iBoxfnLN7/HhCO6";
        user.dateCreation = new Date();
        user.save();

        user.applyNewPlayerSettings();
        user.save();

        // Actions
        List<Action> actions = Action.find.all();
        if (actions.size() > 0) {
            Ebean.delete(actions);
        }
        Action action;
        action = new Action(MapObjectType.NORTH_PASS, 20, "goTo"); action.save();
        action = new Action(MapObjectType.EAST_PASS, 20, "goTo"); action.save();
        action = new Action(MapObjectType.SOUTH_PASS, 20, "goTo"); action.save();
        action = new Action(MapObjectType.WEST_PASS, 20, "goTo"); action.save();

    }
}
