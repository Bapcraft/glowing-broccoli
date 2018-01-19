package org.bapcraft.glowingbroccoli;

import org.bapcraft.glowingbroccoli.config.GbLobbyConfig;
import org.bapcraft.glowingbroccoli.config.GbRootConfig;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class GbListener {

	private GbRootConfig config;
	private Game game;
	private Logger logger;
	
	public GbListener(GbRootConfig c, Game g, Logger l) {
		this.config = c;
		this.game = g;
		this.logger = l;
	}
	
	@Listener
	public void onPlayerMove(MoveEntityEvent event) {
		
		Entity e = event.getTargetEntity();
		
		if (e instanceof Player) {
			
			Player p = (Player) e;
			World w = p.getLocation().getExtent();
			
			for (GbLobbyConfig lc : this.config.worldConfigs) {
				
				if (w.getName().equals(lc.world) && p.getLocation().getBlockY() < lc.teleportHeight) {
					
					// TODO Actually look up their spawn and teleport them.
					
				}
				
			}
			
		}
		
	}
	
	@Listener
	public void onPlayerDeath(DestructEntityEvent.Death event) {
		
		Entity e = event.getTargetEntity();
		
		if (e instanceof Player) {
			
			Player p = (Player) e;
			Location<World> l = p.getLocation();
			World w = l.getExtent();
			
			for (GbLobbyConfig lc : this.config.worldConfigs) {
				
				if (lc.playWorlds.contains(w.getName())) {
					
					// TODO Send the player to the lobby.
					
				}
				
			}
			
		}
		
	}
	
}
