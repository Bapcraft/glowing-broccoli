package org.bapcraft.glowingbroccoli;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.ValueContainer;

import com.flowpowered.math.vector.Vector3f;

public class SpawnDataContainer implements ValueContainer {

	private Map<String, Vector3f> spawnPositions;
	
	public SpawnDataContainer(Map<String, Vector3f> poses) {
		this.spawnPositions = poses;
	}
	
	@Override
	public Optional get(Key key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional getValue(Key key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supports(Key key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ValueContainer copy() {
		return new SpawnDataContainer(new HashMap<>(this.spawnPositions));
	}

	@Override
	public Set getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set getValues() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
