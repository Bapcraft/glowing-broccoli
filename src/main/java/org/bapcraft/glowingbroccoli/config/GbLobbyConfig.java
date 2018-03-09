package org.bapcraft.glowingbroccoli.config;

import java.util.List;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class GbLobbyConfig {

	@Setting(value = "height")
	public int teleportHeight = 32;
	
	@Setting
	public int borderBuffer = 128;
	
	@Setting(value = "lobby")
	public String world;
	
	@Setting(value = "worlds")
	public List<String> playWorlds;
	
}
