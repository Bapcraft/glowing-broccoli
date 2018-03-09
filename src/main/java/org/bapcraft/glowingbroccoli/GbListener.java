package org.bapcraft.glowingbroccoli;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import org.bapcraft.glowingbroccoli.config.GbLobbyConfig;
import org.bapcraft.glowingbroccoli.config.GbRootConfig;
import org.bapcraft.glowingbroccoli.data.UserProfile;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

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
					
					File userFile = new File(new File(game.getSavesDirectory().toFile(), "gbdata"), String.format("spawn-%s.json", p.getUniqueId()));
					Gson gson = makeGson();
					
					if (userFile.exists()) {
						
						try {
							
							// Load the user data from file.
							FileReader fr = new FileReader(userFile);
							UserProfile profile = gson.fromJson(fr, UserProfile.class);
							fr.close();
							
							// Then spawn them there.
							Location<World> sl = w.getLocation(profile.x, profile.y, profile.z);
							p.setLocationSafely(sl);
							
						} catch (JsonSyntaxException | JsonIOException | IOException ex) {
							this.logger.error("Error spawning player " + p.getName() + "!", ex);
							p.sendMessage(Text.of("Error spawning player, talk to an administrator."));
						}
						
					} else {
						
						this.logger.info("Generating new spawn location for player " + p.getName());
						
						// Do a bunch of math to get create a world spawn thingy.
						WorldBorder wb = w.getWorldBorder();
						Random r = new Random();
						double x = wb.getCenter().getX() + (wb.getDiameter() - lc.borderBuffer) * (r.nextDouble() * 2D - 1D);
						double z = wb.getCenter().getZ() + (wb.getDiameter() - lc.borderBuffer) * (r.nextDouble() * 2D - 1D);
						int y = w.getHighestYAt((int) x, (int) z);
						UserProfile created = new UserProfile((int) x, y, (int) z);
						
						try {

							// Now just write it to file.
							FileWriter fw = new FileWriter(userFile);
							fw.write(gson.toJson(created));
							fw.close();
							
						} catch (IOException ex) {
							this.logger.error("Error writing new player spawn for " + p.getName() + "!", ex);
							p.sendMessage(Text.of("Error finding spawn location, talk to an administrator."));
						}
						
					}
					
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
					
					Optional<World> ow = this.game.getServer().getWorld(lc.world);
					if (ow.isPresent()) {
						
						// TODO See how this works with the `/back` command.
						
					} else {
						this.logger.warn("Can't find lobby world " + lc.world + " to teleport player " + p.getName() + " to!");
					}
					
				}
				
			}
			
		}
		
	}
	
	private static Gson makeGson() {
		return new GsonBuilder().setPrettyPrinting().setLenient().create();
	}
	
}
