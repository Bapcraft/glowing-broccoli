package org.bapcraft.glowingbroccoli.config;

import java.util.ArrayList;
import java.util.List;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class GbRootConfig {

	@Setting(value = "lobbies")
	public List<GbLobbyConfig> worldConfigs = new ArrayList<>();
	
}
