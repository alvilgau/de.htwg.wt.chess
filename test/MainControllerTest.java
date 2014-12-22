import org.junit.*;

import play.mvc.*;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import controllers.routes;

public class MainControllerTest {

	@Before
	public void setUp() {
		start(fakeApplication());
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

	// @Test
	// public void testSuccessLogin() {
	// Map<String, String> loginData = new HashMap<String, String>();
	// loginData.put("email", "admin@chess.de");
	// loginData.put("password", "admin");
	// Result result = callAction(routes.ref.MainController.authenticate(),
	// fakeRequest().withFormUrlEncodedBody(loginData));
	// }
}
