package models;

import org.junit.Test;
import play.test.FakeApplication;
import play.test.Helpers;

import java.util.ArrayList;

/**
 * Created by OM on 11.06.2014.
 */
public class LabyrinthTest {
    @Test
    public void testCreateLabyrinth() throws Exception {
        FakeApplication app = Helpers.fakeApplication();
        Helpers.start(app);

        User user = User.find.byId(1L);

        Labyrinth.createLabyrinth();

        user.enterLabyrinth(Labyrinth.find.byId(1L));
        System.out.println("Cell " + user.labyrinth.cells.get(0));

        System.out.println("AFTER CREATE");
        System.out.println(User.find.all());
        System.out.println();
        System.out.println(Labyrinth.find.all());
        System.out.println();
        System.out.println(LabyrinthCell.find.all());
        System.out.println();

        Labyrinth labyrinth = user.labyrinth;
        user.exitLabyrinth();
        Labyrinth.deleteLabyrinth(labyrinth);

        System.out.println("AFTER DELETE");
        System.out.println(Labyrinth.find.all());
        System.out.println();
        System.out.println(LabyrinthCell.find.all());
        System.out.println();
        Helpers.stop(app);
    }
}
