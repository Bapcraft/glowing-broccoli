package org.bapcraft.glowingbroccoli.data;

import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

public interface GbSpawnData {

	Vector3d getSpawn(World w);
	
}
