package org.bapcraft.glowingbroccoli.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

public class SpawnData extends AbstractSingleData<Map<String, Vector3d>, SpawnData, ImmutableSpawnData> {

	protected SpawnData(Map<String, Vector3d> value) {
		super(value, BroccKeys.SPAWNDATA);
	}

	@Override
	public Optional<SpawnData> fill(DataHolder dataHolder, MergeFunction overlap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<SpawnData> from(DataContainer container) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpawnData copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getContentVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected Value<?> getValueGetter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImmutableSpawnData asImmutable() {
		return new ImmutableSpawnData(this.getValue());
	}

	public Vector3d getSpawn(World w) {
		return this.getValue().get(w.getName());
	}
	
	public void setSpawn(World w, Vector3d pos) {
		
		// Not sure if this is 100% correct.  Not sure how the aliasing works.
		Map<String, Vector3d> v = new HashMap<>(this.getValue());
		v.put(w.getName(), pos);
		this.setValue(v);
		
	}
	
}
