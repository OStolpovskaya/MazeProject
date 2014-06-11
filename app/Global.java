import models.User;
import play.GlobalSettings;
import play.Logger;

import java.util.Date;

/**
 * Created by OM on 08.06.2014.
 */
public class Global extends GlobalSettings {
    @Override
    public void onStart(play.Application app) {
        Logger.info("MazeProject has been started");

        User user = User.findByEmail("o.stolpovskaya@gmail.com");

        if (user == null) {
            Logger.debug("Create main user");
            user = new User();
            user.email = "o.stolpovskaya@gmail.com";
            user.fullname = "Olga M";
            user.validated = true;
            user.passwordHash = "$2a$10$TLTjdfzdJ3Gl7bnf7estt.PjxLkQHhXJhsIku6iBoxfnLN7/HhCO6";
            user.dateCreation = new Date();
            user.save();

            user.applyNewPlayerSettings();
            user.save();
        } else {
            Logger.debug("Main user exists");
        }
    }
}
