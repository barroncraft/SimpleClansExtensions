package com.barroncraft.sce;

import java.util.logging.Logger;

import net.sacredlabyrinth.phaed.simpleclans.*;
import net.sacredlabyrinth.phaed.simpleclans.managers.*;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleClansExtensions extends JavaPlugin
{
	private Location redSpawn;
	private Location blueSpawn;
	
	private World world;
	private Logger log;
	
	public void onEnable()
	{
		
	    
	    world = this.getServer().getWorld("dota");
	    
	    redSpawn = new Location(world, -1189, 50, 444);
	    blueSpawn = new Location(world, -943, 50, 187);
	    
		log = this.getLogger();
		log.info("SimpleClanExtensions has been enabled");
	}
	
	public void onDisable()
	{
		log.info("SimpleClanExtensions has been disabled");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		String commandName = cmd.getName();
		SimpleClans clans = (SimpleClans)getServer().getPluginManager().getPlugin("SimpleClans");
		ClanManager manager = clans.getClanManager();
		
		// Only take a command of 'sce' 
		if (commandName.equalsIgnoreCase("sce"))
		{
			if (args.length == 2 && args[0].equalsIgnoreCase("join"))
			{
				String clanName = args[1];
				ClanPlayer clanPlayer = manager.getCreateClanPlayer(sender.getName());
				if (clanPlayer.getClan() == null)
				{
					Clan clan = manager.getClan(clanName);
					if (clan == null)
					{
						manager.createClan(clanPlayer.toPlayer(), clanName, clanName);
						clan = manager.getClan(clanName);
						if (clanName.equalsIgnoreCase("red"))
							clan.setHomeLocation(redSpawn);
						else if (clanName.equalsIgnoreCase("blue"))
							clan.setHomeLocation(blueSpawn);
					}
					else
						clan.addPlayerToClan(clanPlayer);
					
					clanPlayer.toPlayer().teleport(clan.getHomeLocation());
					
					sender.sendMessage(ChatColor.BLUE + "You have joined team " + clanName);
				}
				else
					sender.sendMessage(ChatColor.RED + "You are already on a team");
				return true;
			}
		}
		
		return false;
	}
}
