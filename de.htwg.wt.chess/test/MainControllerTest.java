import static org.fest.assertions.Assertions.assertThat;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.callAction;
import static play.test.Helpers.charset;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import static play.test.Helpers.status;

import org.junit.Test;

import play.mvc.Content;
import play.mvc.Result;
import controllers.routes;

/**
 *
 * Simple (JUnit) tests that can call all parts of a Play app. If you are
 * interested in mocking a whole application, see the wiki for more details.
 *
 */
public class MainControllerTest {

	@Test
	public void simpleCheck() {
		int a = 1 + 1;
		assertThat(a).isEqualTo(2);
	}

	@Test
	public void indexTemplateShouldContainTheStringThatIsPassedToIt() {
		running(fakeApplication(), new Runnable() {
			public void run() {
				Content html = views.html.index.render(
						"Your new application is ready.", null);
				assertThat(contentType(html)).isEqualTo("text/html");
				assertThat(contentAsString(html)).contains(
						"Your new application is ready.");
			}
		});
	}

	@Test
	public void indexShouldContainTheCorrectString() {
		running(fakeApplication(), new Runnable() {
			public void run() {
				Result result = callAction(routes.ref.MainController.index());
				assertThat(status(result)).isEqualTo(OK);
				assertThat(contentType(result)).isEqualTo("text/html");
				assertThat(charset(result)).isEqualTo("utf-8");
				assertThat(contentAsString(result)).contains("Hello Chess");
			}
		});
	}

}
