package com.example;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.api.ChatMessageType;
import net.runelite.client.callback.ClientThread;
import net.runelite.api.Actor;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Combat Chat"
)
public class CombatChatPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private CombatChatConfig config;

	// Variables
	private boolean recentlyLoggedIn;
	private int     ticksSinceLogin = 0;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Combat Chat Plugin has started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Combat Chat Plugin stopped!");
	}

	@Subscribe
	public void onGameTick(GameTick gameTick) {
		recentlyLoggedIn = false;
		ticksSinceLogin++;

		//  We wait for five ticks to pass, to ensure we have a valid player name.
		if(ticksSinceLogin <= 1){
			Player player = client.getLocalPlayer();
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Testing Combat Chat:  " + player.getName(), null);
		}
	}


	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			recentlyLoggedIn = true;
		} else {
			return;
		}
	}

	@Provides
	CombatChatConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CombatChatConfig.class);
	}
}
