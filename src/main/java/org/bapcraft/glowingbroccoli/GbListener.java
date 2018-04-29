package org.bapcraft.glowingbroccoli;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bapcraft.glowingbroccoli.config.GbRootConfig;
import org.bapcraft.glowingbroccoli.data.UserProfile;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
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
	
	private Set<UUID> needsBuffs = new HashSet<>();
	
	public GbListener(GbRootConfig c, Game g, Logger l) {
		this.config = c;
		this.game = g;
		this.logger = l;
	}
	
	@Listener
	public void onPlayerLogin(ClientConnectionEvent.Login event) {
		
		User u = event.getTargetUser();
		World w = event.getToTransform().getExtent();
		
		if (!this.getUserDataFile(u.getUniqueId()).exists()) {
			
			UserProfile profile = null;
			try {
				profile = this.findUserProfileForWorld(w, u.getUniqueId());
			} catch (IOException e) {
				this.logger.error("Could not load player custom spawn data!", e);
				event.setMessage(Text.of("Error!"), Text.of("Tell an admin on the Discord that there was a problem setting your custom spawn!"));
				event.setCancelled(true);
				return;
			}
			
			// Set up the chunks to generate so we can hopefully catch the player early.
			w.newChunkPreGenerate(new Vector3d(profile.x, profile.y, profile.z), 16)
				.owner(this.game.getPluginManager().getPlugin("glowbroc").get())
				.logger(this.logger)
				.chunksPerTick(0) // As fast as possible.
				.start();
			
			// Actually set the spawn location.
			event.setToTransform(createNewTransformForUserProfile(w, profile));
			
			// Make sure to buff the player once they actually join in case there's a fall.
			this.needsBuffs.add(u.getUniqueId());
			
		}
		
	}
	
	@Listener
	public void onPlayerJoin(ClientConnectionEvent.Join event) {
		
		Player p = event.getTargetEntity();
		
		// We need to give the player some extra status effects in case their random spawn is somewhere pathological.
		if (this.needsBuffs.contains(p.getUniqueId())) {
			
			this.needsBuffs.remove(p.getUniqueId());
			
			PotionEffect regen = PotionEffect.builder()
					.potionType(PotionEffectTypes.REGENERATION)
					.duration(30 * 20)
					.amplifier(3)
					.build();
			
			PotionEffect resist = PotionEffect.builder()
					.potionType(PotionEffectTypes.RESISTANCE)
					.duration(30 * 20)
					.amplifier(5)
					.build();
			
			// Actually apply the status effects.
			PotionEffectData ped = p.getOrCreate(PotionEffectData.class).get();
			ped.addElement(regen);
			ped.addElement(resist);
			p.offer(ped);
			
		}
		
	}
	
	@Listener
	public void onPlayerRespawn(RespawnPlayerEvent event) {
		
		Entity e = event.getTargetEntity();
		
		if (e instanceof Player) {
			
			Player p = (Player) e;
			World w = event.getToTransform().getExtent();
			
			if (event.isBedSpawn()) {
				return; // We don't do anything here.
			}
			
			try {
				
				UserProfile profile = this.findUserProfileForWorld(w, p.getUniqueId());
				if (profile == null) {
					this.logger.error("user profile is null, this should never happen");
					p.sendMessage(Text.of("Tell an admin that there was an error writing your user custom spawn data."));
					return;
				}
				
				// Actually set the spawn location.
				event.setToTransform(createNewTransformForUserProfile(w, profile));
				
			} catch (IOException ex) {
				this.logger.warn("Error loading user profile from file.", ex);
				p.sendMessage(Text.of("Error setting custom spawn location.  Tell an admin to check the logs!  (Note the time!)"));
			}
			
		}
		
	}
	
	private static Transform<World> createNewTransformForUserProfile(World w, UserProfile up) {
		int maxY = w.getHighestYAt(up.x, up.z);
		Vector3d pos = new Vector3d(up.x, maxY >= 32 ? maxY : 256, up.z);
		Vector3d dir = Vector3d.createDirectionDeg(Math.random() * 360D, 0D);
		return new Transform<>(w, pos, dir);
	}
	
	private File getUserDataFile(UUID uuid) {
		return new File(new File(game.getSavesDirectory().toFile(), "gbdata"), String.format("spawn-%s.json", uuid));
	}
	
	private UserProfile generateNewSpawnObject(World w) {
		
		// Do a bunch of math to get create a world spawn thingy.
		Random r = new Random();
		WorldBorder wb = w.getWorldBorder();
		double radius = Math.min(wb != null ? wb.getDiameter() : 1e6D, 10000D) / 2D;
		double centerX = wb != null ? wb.getCenter().getX() : w.getSpawnLocation().getX();
		double centerZ = wb != null ? wb.getCenter().getZ() : w.getSpawnLocation().getZ();
		double x = centerX + (radius - this.config.borderBuffer) * (r.nextDouble() * 2D - 1D);
		double z = centerZ + (radius - this.config.borderBuffer) * (r.nextDouble() * 2D - 1D);
		double y = 120; // TODO Make this actually work out, but later.
		
		return new UserProfile((int) x, (int) y, (int) z);
		
	}
	
	private UserProfile findUserProfileForWorld(World w, UUID uuid) throws IOException {

		File userFile = this.getUserDataFile(uuid);
		UserProfile profile = null;
		Gson gson = makeGson();
		
		if (userFile.exists()) {
			
			// Just read it, dumbly.
			try (FileReader fr = new FileReader(userFile)) {
				profile = gson.fromJson(fr, UserProfile.class);
			} catch (JsonSyntaxException | JsonIOException ex) {
				throw new IOException("Error parsing JSON", ex);
			}
			
		} else {
			
			profile = generateNewSpawnObject(w);
			
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
