SimpleClans Extensions
=======================

This plugin is used to extend the functionality of [SimpleClans](http://dev.bukkit.org/bukkit-plugins/simpleclans/) to make is easier to use in a PvP setting (namely [Minecraft DOTA](https://github.com/barroncraft/minecraft-dota)).

Commands
--------

### /sce join [team]

Join the specified team.  If no team is supplied, the team with the least amount of players will be joined.  In order for players to join teams, you will need to list them in config.yml.

### /sce towers

Draw a map of all the towers in the chat window.  Uses `@` symbols for alive towers and `X` for dead towers.

### /sce surrender

If 2/3 of a team vote to surrender, will announce the end of the game and trigger a reset.

Configuration
-------------

### joinDifference (int)

If `teamBalancing` is enabled, this determines the maximum difference between the players on each team.  If a team goes over this, players won't be allowed to join it.

### teamBalancing (boolean)

Enable or disable team balancing.

### resetCommand (String)

A command to run when the game is over and needs to be reset.  If this option is omitted, a file called `reset-required` will be written to the root directory of the server when the game ends.

### resetDelay (int)

The time in ticks the server should wait before resetting

### resetTeams (boolean)

If true, all teams will be disbanded at the end of the game.

### world (String)

The world that everything is happening in.

### clans (List)

A list of clans that will be treated as teams.  For each team, there are a series of options:

#### baseRegion (String)

The WorldGuard region that should be considered the teams base.  Until all towers are destroyed, anyone from the opposing team entering here will be instantly killed.

#### spawnRegion (String)

The WorldGuard region that should be considered the teams spawn area.  Anyone on the opposing team entering here will be instantly killed.

#### spawn (Coords)

Specified as `spawn: {x, y, z}` the area where players from the team should respawn when killed.

Towers
------

This plugin keeps track of all the towers around the map.  Their locations are currently [hard coded in](https://github.com/barroncraft/SimpleClansExtensions/blob/master/src/main/java/com/barroncraft/sce/ExtensionsListener.java#L47) but will probably be moved to the configuration file.  Each tower has a location associated with it that will be watched for a TNT explosion.  If an explosion happens in the area directly surrounding that point, the tower will be considered to be destroyed.  This event is announced in chat and the number of towers remaining for a team will be decreased.  When a team no longer has towers, their base will be open to the enemy team.  Once their nexus is destroyed, the end of the game will be announced and whatever command was specified in config.yml will be run.  

Minecart Destruction
--------------------

There are [two changes](https://github.com/barroncraft/SimpleClansExtensions/blob/master/src/main/java/com/barroncraft/sce/ExtensionsListener.java#L110) to vanilla Minecraft mechanics made by this plugin to fix bugs.  The first prevents Minecarts from being broken unless the attacker has a bow and at least one arrow in their inventory.  This prevents players from glitching inside towers and punching out the minecarts before they buy a bow and find an arrow.  The second fix involves issues with detector rails and minecarts.  For some reason, when the minecarts on the DOTA map are destroyed, the detector rails won't always turn off.  To fix this, whenever a minecart on top of a detector rail is destroyed, it will break the block underneath it.  This block update event will cause the redstone to turn off properly.

