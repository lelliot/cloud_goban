package org.ligi.cloudgoban;

import java.io.IOException;
import java.util.List;

import javax.inject.Named;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;

@Api(name = "cloudgoban")
public class GamesEndpoint {

	@ApiMethod(name = "games.get")
	public Game get(@Named("game_key") String game_key) {
		PersistenceManager pm = getPersistenceManager();
		Game game = pm.getObjectById(Game.class, game_key);
		pm.close();
		return game;
	}

	@ApiMethod(name = "games.insert")
	public Game insert(Game game) {
		PersistenceManager pm = getPersistenceManager();
		pm.makePersistent(game);
		pm.close();
		return game;
	}

	@SuppressWarnings("unchecked")
	@ApiMethod(name = "games.edit")
	public Game edit(Game src_game) {
		PersistenceManager pm = getPersistenceManager();
		Game game = pm.getObjectById(Game.class, src_game.getEncodedKey());

		game.setSgf(src_game.getSgf());
		pm.makePersistent(game);

		// TODO write history?!

		Query query = pm.newQuery(GoGameParticipation.class);
		query.setFilter("gameKey == '" + src_game.getEncodedKey() + "'");
		Sender sender = new Sender("AIzaSyBiVYb5fqWWiviUrOKye2RLleTvF7H8ILc");

		for (GoGameParticipation notification : (List<GoGameParticipation>) pm.newQuery(query).execute()) {
			Message message = new Message.Builder().addData("role", notification.getRole()).addData("game_key", game.getEncodedKey()).build();

			try {
				sender.send(message, notification.getContact(), 42);
			} catch (IOException e) {
			}

			notification.getContact();
		}

		pm.close();
		return game;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
