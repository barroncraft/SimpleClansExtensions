package com.barroncraft.sce;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import com.barroncraft.sce.ClanBuildingList.BuildingType;

public class ExtensionsListener implements Listener {
	
	SimpleClansExtensions plugin;
	ClanBuildingList redBuildings;
	ClanBuildingList blueBuildings;
	
	public ExtensionsListener(SimpleClansExtensions plugin)
	{
		this.plugin = plugin;
		this.redBuildings = new ClanBuildingList();
		this.blueBuildings = new ClanBuildingList();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		loadRedBuildingLocs();
		loadBlueTowerLocs();
	}

	private void loadRedBuildingLocs() 
	{
		redBuildings.AddBuilding(BuildingType.Tower, new Location(null, -1189, 53, 347));
		redBuildings.AddBuilding(BuildingType.Tower, new Location(null, -1190, 53, 257));
		redBuildings.AddBuilding(BuildingType.Tower, new Location(null, -1089, 53, 458));
		redBuildings.AddBuilding(BuildingType.Tower, new Location(null, -1001, 53, 448));
		redBuildings.AddBuilding(BuildingType.Tower, new Location(null, -1091, 54, 339));
		redBuildings.AddBuilding(BuildingType.Nexus, new Location(null, -1159, 55, 410));
	}
	
	private void loadBlueTowerLocs() 
	{
		blueBuildings.AddBuilding(BuildingType.Tower, new Location(null, -1046, 53, 184));
		blueBuildings.AddBuilding(BuildingType.Tower, new Location(null, -1134, 53, 194));
		blueBuildings.AddBuilding(BuildingType.Tower, new Location(null, -939, 53, 288));
		blueBuildings.AddBuilding(BuildingType.Tower, new Location(null, -938, 53, 378));
		blueBuildings.AddBuilding(BuildingType.Tower, new Location(null, -1044, 54, 296));
		blueBuildings.AddBuilding(BuildingType.Nexus, new Location(null, -976, 54, 225));
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) 
	{
		// Check if a TNT has exploded at a know Tower location
		if (event.getEntityType() == EntityType.PRIMED_TNT) 
		{
			Location eventLoc = event.getLocation();
			Server server = plugin.getServer();
			
			// Red towers
			if (redBuildings.DestroyBuilding(BuildingType.Tower, eventLoc))
			{
				server.broadcastMessage(ChatColor.YELLOW + "A "+ ChatColor.RED + "RED" + ChatColor.YELLOW + " Tower Has Been Destroyed!");

				// Check if all towers are destroyed
				if (redBuildings.BuildingsAliveCount(BuildingType.Tower) == 0) 
					server.broadcastMessage(ChatColor.YELLOW + "All "+ ChatColor.RED + "RED" + ChatColor.YELLOW + " Towers Are Destroyed! Defend the NEXUS!");
			}
			
			// Blue Towers
			if (blueBuildings.DestroyBuilding(BuildingType.Tower, eventLoc))
			{
				server.broadcastMessage(ChatColor.YELLOW + "A "+ ChatColor.BLUE + "BLUE" + ChatColor.YELLOW + " Tower Has Been Destroyed!");

				// Check if all towers are destroyed
				if (blueBuildings.BuildingsAliveCount(BuildingType.Tower) == 0) 
				server.broadcastMessage(ChatColor.YELLOW + "All "+ ChatColor.BLUE + "BLUE" + ChatColor.YELLOW + " Towers Are Destroyed! Defend the NEXUS!");
			}
			
			
			// Nexus
			if (redBuildings.DestroyBuilding(BuildingType.Nexus, eventLoc))
			{
				server.broadcastMessage(ChatColor.RED + "The RED NEXUS Has Been Destroyed!" + ChatColor.BLUE + " BLUE WINS!");
			}
			else if (blueBuildings.DestroyBuilding(BuildingType.Nexus, eventLoc))
			{
				server.broadcastMessage(ChatColor.BLUE + "The BLUE NEXUS Has Been Destroyed!" + ChatColor.RED + " RED WINS!");
			}
			
		}
		
	}
	
	
}
