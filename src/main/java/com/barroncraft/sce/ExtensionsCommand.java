package com.barroncraft.sce;

import com.barroncraft.sce.ClanBuildingList.BuildingType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.ClanPlayer;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.kitteh.tag.TagAPI;

public class ExtensionsCommand 
{
    SimpleClansExtensions plugin;
    Set<String> surrenderPlayers;
    ServerReseter resetter;

    public ExtensionsCommand(SimpleClansExtensions plugin)
    {
        this.plugin = plugin;
        this.surrenderPlayers = new HashSet<String>();
        this.resetter = new ServerReseter(plugin);
    }

    public void CommandJoinRandom(Player player)
    {
        Set<String> teams = plugin.getExtensionsConfig().getClanTeams().keySet();
        int currentCount = Integer.MAX_VALUE;
        String currentTeam = "";

        for (String team : teams)
        {
            Clan clan = plugin.getClanManager().getClan(team);
            int count = GetOnlinePlayerCount(clan);
            if (count < currentCount)
            {
                currentTeam = team;
                currentCount = count;
            }
        }

        if (!currentTeam.isEmpty())
            CommandJoin(player, currentTeam);
        else
            player.sendMessage(ChatColor.RED + "No teams to join...");
    }

    public void CommandJoin(Player player, String newClanName)
    {
        ClanPlayer clanPlayer = plugin.getClanManager().getCreateClanPlayer(player.getName());
        Clan newClan = plugin.getClanManager().getClan(newClanName);
        Clan oldClan = clanPlayer.getClan();
        int maxDifference = plugin.getExtensionsConfig().getMaxDifference();
        int transferDifference = maxDifference + 1;

        if (oldClan != null) // Changing clans
        {
            if (newClan != null && oldClan.getName().equalsIgnoreCase(newClan.getName()))
                player.sendMessage(ChatColor.RED + "You can't transfer to the same team");
            else if (plugin.getExtensionsConfig().teamBalancingEnabled() && PlayerDifference(oldClan, newClan) < transferDifference)
                player.sendMessage(ChatColor.RED + "You can't transfer teams unless there is a difference of " + transferDifference + " between them");
            else
            {
                oldClan.removeMember(player.getName());
                newClan = AddPlayerToClan(clanPlayer, newClan, newClanName);
                plugin.getServer().broadcastMessage(ChatColor.YELLOW + player.getName() + " has transfered to team " + newClan.getColorTag());
            }
        } 
        else // Joining a clan for the first time
        {
            if (plugin.getExtensionsConfig().teamBalancingEnabled() && PlayerDifference(newClan, maxDifference) >= maxDifference)
                player.sendMessage(ChatColor.RED + "That team already has too many players.  Try a different one.");
            else
            {
                newClan = AddPlayerToClan(clanPlayer, newClan, newClanName);
                player.getInventory().addItem(new ItemStack(Material.STONE_SWORD, 1));
                plugin.getServer().broadcastMessage(ChatColor.YELLOW + player.getName() + " has joined team " + newClan.getColorTag());
            }
        }
    }

    public void CommandSurrender(Player player) 
    {
        String playerName = player.getName();
        Clan currentClan = plugin.getClanManager().getCreateClanPlayer(playerName).getClan();

        if (currentClan == null)
        {
            player.sendMessage(ChatColor.RED + "You can't surrender until you join a team");
            return;
        }

        if (!surrenderPlayers.contains(playerName))
        {
            surrenderPlayers.add(playerName);

            List<ClanPlayer> currentClanPlayers = currentClan.getOnlineMembers();
            List<ClanPlayer> surrenderingPlayers = new ArrayList<ClanPlayer>();

            for (ClanPlayer clanPlayer : currentClan.getOnlineMembers())
            {
                if (surrenderPlayers.contains(clanPlayer.getName()))
                    surrenderingPlayers.add(clanPlayer);
            }

            double surrenderRatio = ((double)surrenderingPlayers.size()) / ((double)currentClanPlayers.size());

            if (surrenderRatio > 0.66d)
            {
                if (!resetter.getResetFlag())
                {
                    resetter.reset();
                    ClanTeam team = plugin.getExtensionsConfig().getClanTeams().get(currentClan.getName());
                    String teamName = team.getColor() + team.getName().toUpperCase() + ChatColor.YELLOW;
                    plugin.getServer().broadcastMessage(ChatColor.YELLOW + "The " + teamName + " team has agreed to surrender.  Game over.");
                    plugin.getServer().broadcastMessage(ChatColor.YELLOW + "The map should auto reset within a few minutes.");
                }
            }
            else
            {
                player.sendMessage(ChatColor.YELLOW + "You have been added to the list of people wishing to surrender. (" + Math.floor(surrenderRatio * 100) + "% so far)");
                player.sendMessage(ChatColor.YELLOW + "To remove your surrender vote type /surrender again.");
            }
        }
        else
        {
            surrenderPlayers.remove(playerName);
            player.sendMessage(ChatColor.YELLOW + "You have been removed from the list of people wanting to surrender.");
        }
    }

    public void CommandTowers(Player player)
    {
        /* What should be printed:
        +--------------------+
        |##****@****@**|*****|
        |#*@*##########|**@**|
        |***########****\****|
        |*@*#####***@***#----|
        |----#***@***#####*@*|
        |****\****########***|
        |**@**|##########*@*#|
        |*****|**@****@****##|
        +--------------------+
        */

        final String redAlive  = ChatColor.RED   + "@" + ChatColor.WHITE;
        final String blueAlive = ChatColor.BLUE  + "@" + ChatColor.WHITE;
        final String redDead   = ChatColor.GRAY + "X" + ChatColor.WHITE;
        final String blueDead  = ChatColor.GRAY + "X" + ChatColor.WHITE;

        ClanBuildingList redBuildings = plugin.getExtensionsConfig().getClanTeams().get("red").getBuildings();
        String redNexus  = redBuildings.buildingExists(BuildingType.Nexus, 0) ? redAlive : redDead;
        String redTower1 = redBuildings.buildingExists(BuildingType.Tower, 1) ? redAlive : redDead;
        String redTower2 = redBuildings.buildingExists(BuildingType.Tower, 2) ? redAlive : redDead;
        String redTower3 = redBuildings.buildingExists(BuildingType.Tower, 3) ? redAlive : redDead;
        String redTower4 = redBuildings.buildingExists(BuildingType.Tower, 4) ? redAlive : redDead;
        String redTower5 = redBuildings.buildingExists(BuildingType.Tower, 5) ? redAlive : redDead;

        ClanBuildingList blueBuildings = plugin.getExtensionsConfig().getClanTeams().get("blue").getBuildings();
        String blueNexus  = blueBuildings.buildingExists(BuildingType.Nexus, 0) ? blueAlive : blueDead;
        String blueTower1 = blueBuildings.buildingExists(BuildingType.Tower, 1) ? blueAlive : blueDead;
        String blueTower2 = blueBuildings.buildingExists(BuildingType.Tower, 2) ? blueAlive : blueDead;
        String blueTower3 = blueBuildings.buildingExists(BuildingType.Tower, 3) ? blueAlive : blueDead;
        String blueTower4 = blueBuildings.buildingExists(BuildingType.Tower, 4) ? blueAlive : blueDead;
        String blueTower5 = blueBuildings.buildingExists(BuildingType.Tower, 5) ? blueAlive : blueDead;


        String[] towersDiagram = 
        {
            "+--------------------+", 
            "|##       %s       %s   |        |",
            "|#  %s   ##########|   %s   |",
            "|      ########      \\      |",
            "|  %s  #####     %s    #----|",
            "|----#    %s     #####  %s  |",
            "|      \\      ########      |",
            "|   %s   |##########   %s  #|",
            "|        |   %s       %s       ##|",
            "+--------------------+",
        };

        towersDiagram[1] = String.format(towersDiagram[1], redTower2, redTower1);
        towersDiagram[2] = String.format(towersDiagram[2], blueTower5, redNexus);
        towersDiagram[4] = String.format(towersDiagram[4], blueTower4,redTower3);
        towersDiagram[5] = String.format(towersDiagram[5], blueTower3, redTower4);
        towersDiagram[7] = String.format(towersDiagram[7], blueNexus, redTower5);
        towersDiagram[8] = String.format(towersDiagram[8], blueTower1, blueTower2);
            
        for (String line : towersDiagram)
        {
            player.sendMessage(line);
        }
    }

    private int PlayerDifference(Clan clan, int max)
    {
        int returnValue = 0;
        int count = GetOnlinePlayerCount(clan);
        for (String name : plugin.getExtensionsConfig().getClanTeams().keySet())
        {
            int clanCount = GetOnlinePlayerCount(plugin.getClanManager().getClan(name));
            int difference = count - clanCount;
            if (difference >= max)
                return difference;
            else if (difference > returnValue)
                returnValue = difference;
        }

        return returnValue;
    }

    private int PlayerDifference(Clan firstClan, Clan secondClan)
    {
        return GetOnlinePlayerCount(firstClan) - GetOnlinePlayerCount(secondClan);
    }

    private Clan AddPlayerToClan(ClanPlayer player, Clan clan, String clanName)
    {
        if (clan == null)
        {
            ChatColor chatColor = plugin.getExtensionsConfig().getClanTeams().get(clanName).getColor();
            plugin.getClanManager().createClan(player.toPlayer(), chatColor + clanName, clanName);
            clan = plugin.getClanManager().getClan(clanName);
            Location location = plugin.getExtensionsConfig().getClanTeams().get(clanName).getSpawn();
            if (location != null)
                clan.setHomeLocation(location);
        }
        clan.addPlayerToClan(player);
        player.toPlayer().teleport(clan.getHomeLocation());
        TagAPI.refreshPlayer(player.toPlayer());
        return clan;
    }

    private int GetOnlinePlayerCount(Clan clan)
    {
        if (clan == null)
            return 0;

        int count = 0;
        for (ClanPlayer player : clan.getMembers())
        {
            Player onlinePlayer = player.toPlayer();
            if (onlinePlayer != null && onlinePlayer.isOnline())
                count++;
        }
        return count;
    }


}
