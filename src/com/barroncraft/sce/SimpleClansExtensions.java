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

import java.util.Dictionary;
import java.util.HashSet;
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
import org.bukkit.util.Vector;

public class SimpleClansExtensions extends JavaPlugin
{
	public ClanManager manager;
	public Set<String> ClanNames = new HashSet<String>();
	public Dictionary<String, Location> spawnLocations;
	
	private Logger log;
	private World world;
	
	private ExtensionsCommand commandManager;
	
	
	
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
		
		FileConfiguration config = this.getConfig();
		World world = this.getServer().getWorld(config.getString("world"));
		Set<String> teams = config.getConfigurationSection("teams").getKeys(false);
		for (String team : teams)
		{
			ClanNames.add(team);
			Vector location = config.getVector("teams." + team + ".spawn");
			spawnLocations.put(team, new Location(world, location.getX(), location.getY(), location.getZ()));
		}
		
	    // Replace with config file
	    spawnLocations.put("red", new Location(world, -1189, 50, 444));
	    spawnLocations.put("blue", new Location(world, -943, 50, 187));
		
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
			else if (args.length == 2 && args[0].equalsIgnoreCase("join") && ClanNames.contains(args[1]))
			{
				if (player.hasPermission("sce.join"))
					commandManager.CommandJoin(player, args[1]);
				else
					player.sendMessage(ChatColor.RED + "You don't have permission to join teams...");
				return true;
			}
		}
		
		return false;
	}
	

}
