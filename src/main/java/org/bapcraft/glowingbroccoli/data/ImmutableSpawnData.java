package org.bapcraft.glowingbroccoli.data;

import java.util.Map;

import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

public class ImmutableSpawnData extends AbstractImmutableSingleData<Map<String, Vector3d>, ImmutableSpawnData, SpawnData> {

	protected ImmutableSpawnData(Map<String, Vector3d> value) {
		super(value, BroccKeys.SPAWNDATA);
	}

	@Override
	public int getContentVersion() {
		return 1;
	}

	@Override
	protected ImmutableValue<Map<String, Vector3d>> getValueGetter() {
		return this.getValueGetter();
	}

	@Override
	public SpawnData asMutable() {
		return new SpawnData(this.getValue()); // wtf?
	}

	public Vector3d getSpawn(World w) {
		return this.getValue().get(w.getName());
	}
	
}
