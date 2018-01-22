package org.bapcraft.glowingbroccoli.data;

import java.util.Map;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.mutable.Value;

import com.flowpowered.math.vector.Vector3d;
import com.google.common.reflect.TypeToken;

@SuppressWarnings("deprecation")
public class BroccKeys {

	@SuppressWarnings("serial")
	// FIXME This is horribly wrong but I don't know how to make it right.
	public static final Key<? extends BaseValue<Map<String, Vector3d>>> SPAWNDATA =
		KeyFactory.makeSingleKey( // ???
			new TypeToken<Map<String, Vector3d>>() {},
			new TypeToken<Value<Map<String, Vector3d>>>() {},
			DataQuery.of("spawn-data"),
			"glowingboccoli:spawns",
			"Spawn Data");
	
}
