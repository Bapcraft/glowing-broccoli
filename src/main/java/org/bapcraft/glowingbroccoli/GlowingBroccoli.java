package org.bapcraft.glowingbroccoli;

import java.io.IOException;
import java.nio.file.Path;

import org.bapcraft.glowingbroccoli.config.GbRootConfig;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

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
	private Path configPath;
	
	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configLoader;

	private GbListener listener;
	private GbRootConfig config;
	
	@Listener
	public void onPreInit(GamePreInitializationEvent event) {
		
	}
	
	@Listener
	public void onInit(GameInitializationEvent event) throws Exception {
		
		this.logger.info("Congrats on your Glowing Broccoli!");
		
		// Load config.
		Asset cfgAsset = this.game.getAssetManager().getAsset(this, "default.conf").get();
		try {
			
			if (!this.configPath.toFile().exists()) {
				cfgAsset.copyToFile(this.configPath);
			}
			
			ConfigurationNode root = this.configLoader.load();
			this.config = root.getValue(TypeToken.of(GbRootConfig.class));
			
		} catch (IOException e) {
			this.logger.error("Unable to load config!");
			throw e;
		} catch (ObjectMappingException e) {
			this.logger.error("Unable to parse config!");
			throw e;
		}
		
		// Setup listeners.
		this.listener = new GbListener(this.config, this.game, this.logger);
		this.eventManager.registerListeners(this, this.listener);
		
	}
	
}
