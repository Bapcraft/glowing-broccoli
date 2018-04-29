package org.bapcraft.glowingbroccoli.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class GbRootConfig {

	@Setting(value = "borderbuffer")
	public Integer borderBuffer = 128;
	
}
