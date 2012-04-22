/**
 * This plugin is used to extend the functionality of SimpleClans
 * to make is easier to use in a PvP setting.  
 * 
 * Written by:
 * 	Nullreff <rsmendivil.com>
 * 
 * Some code and ideas borrowed from:
 * 	Scyntrus <http://www.minecraftforum.net/user/474851-scyntrus/>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License.  This program 
 * is distributed in the hope that it will be useful,  but WITHOUT ANY 
 * WARRANTY.  See the GNU General Public License for more details.
 */

package com.barroncraft.sce;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import net.sacredlabyrinth.phaed.simpleclans.*;
import net.sacredlabyrinth.phaed.simpleclans.managers.*;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleClansExtensions extends JavaPlugin
{
	public ClanManager manager;
	public Set<String> ClanNames = new HashSet<String>();
	public Map<String, Location> spawnLocations = new HashMap<String, Location>();
	public int maxDifference;
	
	private ExtensionsCommand commandManager;
	private Logger log;
	
	
	public void onEnable()
	{
		log = this.getLogger();
		Plugin clansPlugin = getServer().getPluginManager().getPlugin("SimpleClans");
	    if (clansPlugin == null)
	    {
	    	log.severe("SimpleClans plugin not found.  SimpleClansExtenisons was not enabled.");
	    	return;
	    }
	    manager = ((SimpleClans)clansPlugin).getClanManager();
		commandManager = new ExtensionsCommand(this);
		
		log.info("Loading Config File...");
		FileConfiguration config = this.getConfig();
		config.options().copyDefaults(true);
		
		maxDifference = config.getInt("joinDifference");
		log.info("joinDifference: " + maxDifference);
		
		World world = this.getServer().getWorld(config.getString("world"));
		log.info("world: " + world.getName());
		
		Set<String> clans = config.getConfigurationSection("clans").getKeys(false);
		log.info("Clans (" + clans.size() + "):");
		for (String clan : clans)
		{
			log.info("  " + clan);
			ClanNames.add(clan);
			spawnLocations.put(clan, new Location(world, 
				config.getInt("clans." + clan + ".spawn.x"),
				config.getInt("clans." + clan + ".spawn.y"),
				config.getInt("clans." + clan + ".spawn.z")
			));
		}
		
		log.info("SimpleClanExtensions has been enabled");
	}
	
	public void onDisable()
	{
		log.info("SimpleClanExtensions has been disabled");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		if (!(sender instanceof Player))
			return false;
		
		Player player = (Player)sender;
		String commandName = cmd.getName();
		if (commandName.equalsIgnoreCase("sce"))
		{
			if (manager == null)
			{
				player.sendMessage(ChatColor.RED + "SimpleClans plugin not found...");
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("join"))
			{
				/*if (!player.hasPermission("sce.join"))
					player.sendMessage(ChatColor.RED + "You don't have permission to join teams...");
					
				else*/ if (!ClanNames.contains(args[1]))
					player.sendMessage(ChatColor.RED + "The clan " + args[1] + " doesn't exist.");
				else
					commandManager.CommandJoin(player, args[1]);
				return true;
			}
		}
		
		return false;
	}
	

}
