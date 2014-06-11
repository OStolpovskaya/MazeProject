package models;

import org.junit.Test;
import play.test.FakeApplication;
import play.test.Helpers;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created by OM on 11.06.2014.
 */
public class LabyrinthTest {
    @Test
    public void testUserEnterLabyrinth() throws Exception {
        FakeApplication app = Helpers.fakeApplication();
        Helpers.start(app);

        User user = User.find.byId(1L);

        Labyrinth.createLabyrinth();

        assertThat(Labyrinth.find.all().size()).isEqualTo(1);
        assertThat(LabyrinthCell.find.all().size()).isEqualTo(Labyrinth.N*Labyrinth.N);

        user.enterLabyrinth(Labyrinth.find.byId(1L));
        assertThat(user.labyrinth).isEqualTo(Labyrinth.find.byId(1L));

        Helpers.stop(app);
    }

    @Test
    public void testUserExitLabyrinth() throws Exception {
        FakeApplication app = Helpers.fakeApplication();
        Helpers.start(app);

        User user = User.find.byId(1L);

        Labyrinth.createLabyrinth();
        user.enterLabyrinth(Labyrinth.find.byId(1L));
        user.exitLabyrinth();
        assertThat(user.labyrinth).isEqualTo(null);

        Labyrinth.deleteLabyrinth(Labyrinth.find.byId(1L));
        assertThat(Labyrinth.find.all().size()).isEqualTo(0);
        assertThat(LabyrinthCell.find.all().size()).isEqualTo(0);

        Helpers.stop(app);
    }
}
