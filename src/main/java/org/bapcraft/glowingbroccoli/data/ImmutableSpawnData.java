package org.bapcraft.glowingbroccoli.data;

import java.util.Map;

import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

public class ImmutableSpawnData extends AbstractImmutableData<ImmutableSpawnData, SpawnData> implements GbSpawnData {

	protected Map<String, Vector3d> spawns;
	
	protected ImmutableSpawnData(Map<String, Vector3d> spawns) {
		this.spawns = spawns;
	}

	@Override
	public SpawnData asMutable() {
		return new SpawnData(this.spawns);
	}

	@Override
	public Vector3d getSpawn(World w) {
		return this.spawns.get(w.getName());
	}

	@Override
	public int getContentVersion() {
		return 1;
	}

	@Override
	protected void registerGetters() {
		// TODO Auto-generated method stub
	}
	
}
