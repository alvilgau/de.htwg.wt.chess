import org.junit.*;

import play.mvc.*;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import controllers.routes;

public class MainControllerTest {

	@Before
	public void setUp() {
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
	}

	@After
	public void tearDown() {
		stop(fakeApplication());
	}

	@Test
	public void simpleCheck() {
		int a = 1 + 1;
		assertThat(a).isEqualTo(2);
	}

	@Test
	public void indexShouldContainTheCorrectString() {
		Result result = callAction(routes.ref.MainController.index());
		assertThat(status(result)).isEqualTo(OK);
		assertThat(contentType(result)).isEqualTo("text/html");
		assertThat(charset(result)).isEqualTo("utf-8");
		assertThat(contentAsString(result)).contains("Welcome to Chess");
	}

}
