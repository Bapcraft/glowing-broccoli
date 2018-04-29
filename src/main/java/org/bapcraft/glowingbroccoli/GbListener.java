package org.bapcraft.glowingbroccoli;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import org.bapcraft.glowingbroccoli.config.GbRootConfig;
import org.bapcraft.glowingbroccoli.data.UserProfile;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;

import com.flowpowered.math.vector.Vector3d;
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
	public void onPlayerRespawn(RespawnPlayerEvent event) {
		
		Entity e = event.getTargetEntity();
		
		if (e instanceof Player) {
			
			Player p = (Player) e;
			Location<World> l = event.getToTransform().getLocation();
			World w = event.getToTransform().getExtent();
			
			if (event.isBedSpawn()) {
				return; // We don't do anything here.
			}
			
			try {
				
				// Actually set the spawn location.
				UserProfile profile = this.findUserProfileForWorld(w, p);
				if (profile == null) {
					this.logger.error("user profile is null, this should never happen");
				}
				
				Vector3d pos = new Vector3d(profile.x, w.getHighestYAt(profile.x, profile.z), profile.z);
				Vector3d dir = Vector3d.createDirectionDeg(Math.random() * 360D, 0D);
				Transform<World> changedSpawn = new Transform<>(w, pos, dir);
				event.setToTransform(changedSpawn);
				
			} catch (IOException ex) {
				this.logger.warn("Error loading user profile from file.", ex);
				p.sendMessage(Text.of("Error setting custom spawn location.  Tell an admin to check the logs!  (Note the time!)"));
			}
			
		}
		
	}
	
	private UserProfile findUserProfileForWorld(World w, Player p) throws IOException {

		File userFile = new File(new File(game.getSavesDirectory().toFile(), "gbdata"), String.format("spawn-%s.json", p.getUniqueId()));
		UserProfile profile = null;
		Gson gson = makeGson();
		
		if (userFile.exists()) {
			
			try (FileReader fr = new FileReader(userFile)) {
				profile = gson.fromJson(fr, UserProfile.class);
			} catch (JsonSyntaxException | JsonIOException ex) {
				throw new IOException("Error parsing JSON", ex);
			}
			
		} else {
			
			this.logger.info("Generating new spawn location for player " + p.getName());
			
			// Do a bunch of math to get create a world spawn thingy.
			Random r = new Random();
			WorldBorder wb = w.getWorldBorder();
			double diameter = Math.min(wb != null ? wb.getDiameter() : 1e6D, 10000D);
			double centerX = wb != null ? wb.getCenter().getX() : w.getSpawnLocation().getX();
			double centerZ = wb != null ? wb.getCenter().getZ() : w.getSpawnLocation().getZ();
			double x = centerX + (diameter - this.config.borderBuffer) * (r.nextDouble() * 2D - 1D);
			double z = centerZ + (diameter - this.config.borderBuffer) * (r.nextDouble() * 2D - 1D);
			double y = 120; // TODO Make this actually work out, but later.
			profile = new UserProfile((int) x, (int) y, (int) z);
			
			// Set up the chunk to generate now.
			w.newChunkPreGenerate(new Vector3d(x, y, z), 16).owner(this.game.getPluginManager().getPlugin("glowbroc").get()).logger(this.logger).start();
			
			this.logger.info(String.format("Random spawn for %s is (%s, %s), from center of (%s, %s) and diameter %s.", p.getName(), x, z, centerX, centerZ, diameter));
			
			// Now just write it to file.
			userFile.getParentFile().mkdirs();
			userFile.createNewFile();
			try (FileWriter fw = new FileWriter(userFile)) {
				fw.write(gson.toJson(profile));
			}
			
		}
		
		return profile;
		
	}
	
	private static Gson makeGson() {
		return new GsonBuilder().setPrettyPrinting().setLenient().create();
	}
	
}
