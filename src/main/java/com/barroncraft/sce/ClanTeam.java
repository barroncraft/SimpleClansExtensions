package com.barroncraft.sce;

import org.bukkit.ChatColor;
import org.bukkit.Location;

public class ClanTeam 
{
	public ClanTeam(String name, ChatColor color, Location spawn, String baseRegion, String spawnRegion)
	{
		this.name = name;
		this.color = color;
		this.spawn = spawn;
		this.baseRegion = baseRegion;
		this.spawnRegion = spawnRegion;
		this.buildings = new ClanBuildingList();
	}
	
	private String name;
	private ChatColor color;
	private Location spawn;
	private String baseRegion;
	private String spawnRegion;
	private ClanBuildingList buildings;
	
	public String getName() { return name; }
	public ChatColor getColor() { return color; }
	public Location getSpawn() { return spawn; }
	public String getBaseRegion() { return baseRegion; }
	public String getSpawnRegion() { return spawnRegion; }
	public ClanBuildingList getBuildings() { return buildings; }
}
