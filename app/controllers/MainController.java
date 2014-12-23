package controllers;

import java.util.HashMap;
import java.util.Map;

import models.FieldObserver;
import play.data.Form;
import play.libs.F.Callback0;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.OpenID;
import play.libs.OpenID.UserInfo;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security.Authenticated;
import play.mvc.Security.Authenticator;
import play.mvc.WebSocket;

import com.fasterxml.jackson.databind.JsonNode;

import de.htwg.chess.Chess;
import de.htwg.chess.controller.IChessController;
import de.htwg.util.observer.IObserver;

public class MainController extends Controller {

	static IChessController controller = Chess.getInstance().getController();

	@Authenticated(Secured.class)
	public static Result index() {
		return ok(views.html.index.render());
	}

	public static Result chess() {
		return ok(views.html.chess.render(controller));
	}

	public static Result contact() {
		return ok(views.html.contact.render());
	}

	public static Result handleMovement(String command) {
		int posX = Integer.parseInt(command.substring(0, 1));
		int posY = Integer.parseInt(command.substring(1, 2));
		controller.handleMovement(posX, posY);
		return ok(Json.toJson(controller));
	}

	public static WebSocket<JsonNode> connectWebSocket() {
		return new WebSocket<JsonNode>() {

			public void onReady(WebSocket.In<JsonNode> in,
					WebSocket.Out<JsonNode> out) {

				IObserver fieldObserver = new FieldObserver(controller, out);

				in.onClose(new Callback0() {
					public void invoke() throws Throwable {
						controller.removeObserver(fieldObserver);
					}
				});
			}

		};
	}

	public static Result login() {
		return ok(views.html.login.render(Form.form(Login.class)));
	}

	public static Result logout() {
		session().clear();
		return redirect(routes.MainController.index());
	}

	public static Result authenticate() {
		Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

		if (loginForm.hasErrors()) {
			return badRequest(views.html.login.render(loginForm));
		} else {
			session().clear();
			session("email", loginForm.get().email);
			return redirect(routes.MainController.index());
		}
	}

	public static Result auth() {
		String providerUrl = "https://www.google.com/accounts/o8/id";
		String returnToUrl = routes.MainController.verify().absoluteURL(
				request());

		Map<String, String> attrs = new HashMap<>();
		attrs.put("Email", "http://schema.openid.net/contact/email");
		attrs.put("FirstName", "http://schema.openid.net/namePerson/first");
		attrs.put("LastName", "http://schema.openid.net/namePerson/last");

		Promise<String> redirectUrl = OpenID.redirectURL(providerUrl,
				returnToUrl, attrs);
		return redirect(redirectUrl.get(1000));
	}

	public static Promise<Result> verify() {
		Promise<UserInfo> userInfoPromise = OpenID.verifiedId();

		Promise<Result> resultPromise = userInfoPromise.map(
				new Function<OpenID.UserInfo, Result>() {

					@Override
					public Result apply(UserInfo userInfo) throws Throwable {
						session().clear();
						session("email", userInfo.attributes.get("Email"));
						return redirect(routes.MainController.index());
					}
				}).recover(new Function<Throwable, Result>() {

			@Override
			public Result apply(Throwable throwable) throws Throwable {
				return redirect(routes.MainController.login());
			}
		});

		return resultPromise;
	}

	public static class Login {

		private final static String defaultEmail = "admin@chess.de";
		private final static String defaultPasswort = "admin";

		public String email;
		public String password;

		public String validate() {
			if (email.equals(defaultEmail) && password.equals(defaultPasswort)) {
				return null;
			} else {
				return "Invalid user or password";
			}
		}
	}

	public static class Secured extends Authenticator {

		@Override
		public String getUsername(Context ctx) {
			return ctx.session().get("email");
		}

		@Override
		public Result onUnauthorized(Context ctx) {
			return redirect(routes.MainController.login());
		}
	}

}
