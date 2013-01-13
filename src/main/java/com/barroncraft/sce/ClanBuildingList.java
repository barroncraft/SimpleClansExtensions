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
    private Map<BuildingType, Map<Location, Integer>> buildings;

    public ClanBuildingList()
    {
        buildings = new HashMap<BuildingType, Map<Location, Integer>>();
        for (BuildingType type : BuildingType.values())
            buildings.put(type, new HashMap<Location, Integer>());
    }

    public void addBuilding(BuildingType type, Location location, int index)
    {
        buildings.get(type).put(location, index);
    }

    public boolean buildingExists(BuildingType type, Location location)
    {
        return buildings.get(type).keySet().contains(location);
    }

    public boolean buildingExists(BuildingType type, int index)
    {
        return buildings.get(type).values().contains(index);
    }

    public boolean destroyBuilding(BuildingType type, Location location)
    {
        Set<Location> typeBuildings = buildings.get(type).keySet();
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

    public int buildingsCount(BuildingType type)
    {
        return buildings.get(type).size();
    }
}
