package com.barroncraft.sce;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

public class ExtensionsConfig
{
    private final Map<String, ClanTeam> clanTeams;
    private final int maxDifference;
    private final boolean teamBalancing;
    private final World world;

    public ExtensionsConfig(SimpleClansExtensions plugin)
    {
        Logger log = plugin.getLogger();

        FileConfiguration config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveConfig();

        clanTeams = new HashMap<String, ClanTeam>();

        maxDifference = config.getInt("joinDifference");
        log.info("joinDifference: " + maxDifference);

        teamBalancing = config.getBoolean("teamBalancing");
        log.info("teamBalancing: " + teamBalancing);

        String worldName = config.getString("world");
        world = plugin.getServer().getWorld(worldName);
        if (world == null)
        {
            log.severe("World '" + worldName + "' could not be found.");
            return;
        }
        log.info("world: " + worldName);

        Set<String> clans = config.getConfigurationSection("clans").getKeys(false);
        log.info("Clans (" + clans.size() + "):");
        for (String clan : clans)
        {
            log.info("  " + clan);
            clanTeams.put(clan, new ClanTeam(
                    clan,
                    ChatColor.valueOf(clan.toUpperCase()),
                    new Location(world,
                            config.getInt("clans." + clan + ".spawn.x"),
                            config.getInt("clans." + clan + ".spawn.y"),
                            config.getInt("clans." + clan + ".spawn.z")
                    ),
                    config.getString("clans." + clan + ".baseRegion"),
                    config.getString("clans." + clan + ".spawnRegion")
            ));
        }
    }

    public Map<String, ClanTeam> getClanTeams() { return clanTeams; }
    public int getMaxDifference() { return maxDifference; }
    public boolean teamBalancingEnabled() { return teamBalancing; }
    public World getWorld() { return world; }
}
