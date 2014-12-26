import org.pac4j.core.client.Clients;
import org.pac4j.oauth.client.Google2Client;
import org.pac4j.play.Config;

import play.Application;
import play.GlobalSettings;
import play.Play;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		String baseUrl = Play.application().configuration()
				.getString("baseUrl");

		String googleKey = "918362214996-5o501f5t28sbniscann9op0el2qvk6k1.apps.googleusercontent.com";
		String googleSecret = "-_hsStd_4GDMbq9ljEtORukY";
		Google2Client googleClient = new Google2Client(googleKey, googleSecret);

		Clients clients = new Clients(baseUrl + "/auth", googleClient);
		Config.setClients(clients);
	}
}
