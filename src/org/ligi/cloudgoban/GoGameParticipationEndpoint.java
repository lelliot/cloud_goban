package org.ligi.cloudgoban;

import java.util.List;

import javax.inject.Named;
import javax.jdo.PersistenceManager;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

@Api(name = "cloudgoban")
public class GoGameParticipationEndpoint {

	@ApiMethod(name = "participation.delete")
	public Void delete(@Named("contact") String contact, @Named("game_key") String gameKey) {
		PersistenceManager pm = getPersistenceManager();

		javax.jdo.Query q = pm.newQuery(GoGameParticipation.class);
		q.setFilter("gameKey == '" + gameKey + "' && contact == '" + contact + "'");

		pm.close();

		return null;
	}

	@SuppressWarnings("unchecked")
	@ApiMethod(name = "participation.insert")
	public GoGameParticipation insert(GoGameParticipation game_notification) {
		PersistenceManager pm = getPersistenceManager();

		javax.jdo.Query q = pm.newQuery(GoGameParticipation.class);
		q.setFilter("gameKey == '" + game_notification.getGameKey() + "'");

		for (GoGameParticipation notify : (List<GoGameParticipation>) (pm.newQuery(q).execute())) {
			if (notify.getContact().equals(game_notification.getContact()))
				return notify;

			if (game_notification.getRole().equals("b") && notify.getRole().equals("b"))
				game_notification.setRole("s");

			if (game_notification.getRole().equals("w") && notify.getRole().equals("w"))
				game_notification.setRole("s");
		}

		pm.makePersistent(game_notification);

		pm.close();
		return game_notification;

	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
