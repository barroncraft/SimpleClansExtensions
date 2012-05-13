package com.barroncraft.sce;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;

public class ClanBuildingList 
{
	private final int maxDistance = 1;
	
	public enum BuildingType { Tower, Nexus };
	private Map<BuildingType, Set<Location>> buildings;
	
	public ClanBuildingList()
	{
		buildings = new HashMap<BuildingType, Set<Location>>();
		for (BuildingType type : BuildingType.values())
			buildings.put(type, new HashSet<Location>());
	}
	
	public void AddBuilding(BuildingType type, Location location)
	{
		buildings.get(type).add(location);
	}
	
	public boolean BuildingExists(BuildingType type, Location location)
	{
		return buildings.get(type).contains(location);
	}
	
	public boolean DestroyBuilding(BuildingType type, Location location)
	{
		Set<Location> typeBuildings = buildings.get(type);
		for (Location tower : typeBuildings)
		{
			if (tower.distance(location) <= maxDistance && typeBuildings.contains(tower))
			{
				buildings.get(type).remove(tower);
				return true;
			}
		}
		return false;
	}
	
	public int BuildingsCount(BuildingType type)
	{
		return buildings.get(type).size();
	}
}
