package org.bapcraft.glowingbroccoli;

import org.bapcraft.glowingbroccoli.config.GbRootConfig;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "glowbroc", name = "GlowingBroccoli", version = "0.0.1")
public class GlowingBroccoli {

	@Inject
	private Game game;
	
	@Inject
	private EventManager eventManager;
	
	@Inject
	private Logger logger;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configLoader;

	private GbListener listener;
	private GbRootConfig config;
	
	@Listener
	public void onPreInit(GamePreInitializationEvent event) {
		
	}
	
	@Listener
	public void onInit(GameInitializationEvent event) {
		
		this.logger.info("Congrats on your Glowing Broccoli!");
		
		this.listener = new GbListener(this.config, this.game, this.logger);
		this.eventManager.registerListeners(this, this.listener);
		
	}
	
}
