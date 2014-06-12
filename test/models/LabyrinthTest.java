package models;

import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.*;


import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.callAction;
import static play.test.Helpers.GET;
import static play.test.Helpers.fakeRequest;
import static play.test.Helpers.routeAndCall;

/**
 * Created by OM on 11.06.2014.
 */
public class LabyrinthTest {
    @Test
    public void testUserEnterWorld() throws Exception {
        FakeApplication app = Helpers.fakeApplication();
        Helpers.start(app);

        Result result = routeAndCall(fakeRequest(GET, "/dashboard/enterWorld").withSession("email", "o.stolpovskaya@gmail.com"));
        assertThat(World.find.all().size()).isEqualTo(1);
        assertThat(Labyrinth.find.all().size()).isGreaterThan(0);
        assertThat(LabyrinthCell.find.all().size()).isGreaterThan(0);

        Helpers.stop(app);

    }


    @Test
    public void testUserExitLabyrinth() throws Exception {
        FakeApplication app = Helpers.fakeApplication();
        Helpers.start(app);

        Result result = routeAndCall(fakeRequest(GET, "/dashboard/enterWorld").withSession("email", "o.stolpovskaya@gmail.com"));
        result = routeAndCall(fakeRequest(GET, "/dashboard/exitWorld").withSession("email", "o.stolpovskaya@gmail.com"));

        assertThat(World.find.all().size()).isEqualTo(0);
        assertThat(Labyrinth.find.all().size()).isEqualTo(0);
        assertThat(LabyrinthCell.find.all().size()).isEqualTo(0);

        Helpers.stop(app);
    }

}
