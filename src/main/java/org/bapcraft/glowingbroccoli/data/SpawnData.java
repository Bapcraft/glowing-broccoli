package org.bapcraft.glowingbroccoli.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

public class SpawnData extends AbstractData<SpawnData, ImmutableSpawnData> implements GbSpawnData {

	protected Map<String, Vector3d> spawns;
	
	protected SpawnData(Map<String, Vector3d> spawns) {
		this.spawns = spawns;
	}

	@Override
	public Vector3d getSpawn(World w) {
		return this.spawns.get(w.getName());
	}
	
	public void setSpawn(World w, Vector3d pos) {
		
		// Not sure if this is 100% correct.  Not sure how the aliasing works.
		this.spawns.put(w.getName(), pos);
		
	}

	@Override
	public Optional<SpawnData> fill(DataHolder dataHolder, MergeFunction overlap) {
		this.spawns = dataHolder.get(BroccKeys.SPAWNDATA).get().spawns;
		return Optional.of(this);
	}

	@Override
	public Optional<SpawnData> from(DataContainer container) {
		return container.get(BroccKeys.SPAWNDATA.getQuery()).map(v -> (SpawnData) v);
	}

	@Override
	public SpawnData copy() {
		return new SpawnData(new HashMap<>(this.spawns));
	}

	@Override
	public ImmutableSpawnData asImmutable() {
		return new ImmutableSpawnData(new HashMap<>(this.spawns));
	}

	@Override
	public int getContentVersion() {
		return 1;
	}

	@Override
	protected void registerGettersAndSetters() {
		// TODO
	}
	
}
