package com.barroncraft.sce;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

public class ClanBuildingList 
{
	private final int maxDistance = 0;
	
	public enum BuildingType { Tower, Nexus };
	private Map<BuildingType, Map<Location, Boolean>> buildings;
	
	public ClanBuildingList()
	{
		buildings = new HashMap<BuildingType, Map<Location, Boolean>>();
		for (BuildingType type : BuildingType.values())
			buildings.put(type, new HashMap<Location, Boolean>());
	}
	
	public void AddBuilding(BuildingType type, Location location)
	{
		buildings.get(type).put(location, true);
	}
	
	public boolean BuildingExists(BuildingType type, Location location)
	{
		return buildings.get(type).containsKey(location);
	}
	
	public boolean BuildingAlive(BuildingType type, Location location)
	{
		return buildings.get(type).get(location);
	}
	
	public boolean BuildingExistsAndAlive(BuildingType type, Location location)
	{
		Map<Location, Boolean> typeBuildings = buildings.get(type);
		return typeBuildings.containsKey(location) &&
			   typeBuildings.get(location);
	}
	
	public boolean DestroyBuilding(BuildingType type, Location location)
	{
		Map<Location, Boolean> typeBuildings = buildings.get(type);
		for (Location tower : typeBuildings.keySet())
		{
			if (tower.distance(location) == maxDistance && typeBuildings.get(tower))
			{
				typeBuildings.put(location, false);
				return true;
			}
		}
		return false;
	}
	
	public int BuildingsCount(BuildingType type)
	{
		return buildings.get(type).size();
	}
	
	public int BuildingsAliveCount(BuildingType type)
	{
		int count = 0;
		for (Boolean alive : buildings.get(type).values())
		{
			if (alive)
				count++;
		}
		return count;
	}
}
