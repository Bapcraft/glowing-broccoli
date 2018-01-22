package org.bapcraft.glowingbroccoli.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

public class ImmutableSpawnData extends AbstractImmutableSingleData<Map<String, Vector3d>, SpawnData, ImmutableSpawnData> {

	protected ImmutableSpawnData(Map<String, Vector3d> value) {
		super(value, BroccKeys.SPAWNDATA);
	}

	@Override
	public int getContentVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected ImmutableValue<?> getValueGetter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImmutableSpawnData asMutable() {
		return new ImmutableSpawnData(this.getValue()); // wtf?
	}

	public Vector3d getSpawn(World w) {
		return this.getValue().get(w.getName());
	}
	
}
