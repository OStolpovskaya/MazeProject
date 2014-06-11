package models;

import com.avaje.ebean.Expr;
import play.db.ebean.Model;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import play.Logger;
import scala.util.parsing.combinator.testing.Str;

/**
 * Created by OM on 12.06.2014.
 */

@Entity
public class Action extends Model {
    @Id
    public long id;

    @Enumerated(EnumType.STRING)
    public MapObjectType type;

    public long time;

    private final String methodName;

    public static Model.Finder<Long, Action> find = new Model.Finder<Long, Action>(Long.class, Action.class);

    public Action(MapObjectType type, long time, String methodName) {

        this.type = type;
        this.time = time;
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", type=" + type +
                ", time=" + time +
                ", methodName='" + methodName + '\'' +
                '}';
    }

    public static Action findByMapObjectType(MapObjectType type) {

        return find.where(Expr.eq("type", type)).findUnique();
    }

    public void execute(User user, MapObject mapObject) {
        Method method;
        try {
            method = this.getClass().getMethod(methodName, User.class, MapObject.class);
            method.invoke(this, user, mapObject);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void goTo(User user, MapObject mapObject) {
        Logger.debug(String.format("User %s goes to cell %s. Map object is %s. Spent time: %d", user.fullname, mapObject.param, mapObject.type, time));
        LabyrinthCell newCurrentCell = LabyrinthCell.find.byId(Long.parseLong(mapObject.param));
        newCurrentCell.visited = true;
        newCurrentCell.save();

        Labyrinth labyrinth = user.labyrinth;
        labyrinth.currentCell = newCurrentCell;
        labyrinth.save();
    }
}
