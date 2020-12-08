package com.github.ejitron.helix;

import java.util.Arrays;

import com.github.ejitron.Bot;
import com.github.twitch4j.helix.domain.GameList;

public class Game {
	
	/**
	 * Retrieves the Game ID from a Game Name. Useful in all Helix endpoints since they only listen to game ID's.
	 * @param game the game name. Must be exact match!
	 * @return a game ID as a {@link java.lang.String String} object
	 */
	public String getGameId(String game) {
		GameList resList = Bot.twitchClient.getHelix().getGames(Bot.chatOauth.getAccessToken(), null, Arrays.asList(game)).execute();
		
		if(resList.getGames().size() < 1)
			return null;
		
		return resList.getGames().get(0).getId();
	}

}
