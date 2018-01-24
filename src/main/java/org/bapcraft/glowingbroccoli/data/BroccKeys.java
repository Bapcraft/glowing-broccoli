package org.bapcraft.glowingbroccoli.data;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

import com.google.common.reflect.TypeToken;

public class BroccKeys {

	@SuppressWarnings("serial")
	public static final Key<Value<ImmutableSpawnData>> SPAWNDATA = 
		Key.builder()
			.type(new TypeToken<Value<ImmutableSpawnData>>() {})
			.id("gbspawns")
			.name("Spawn")
			.query(DataQuery.of("GBSpawn"))
			.build();
			
}
