package com.barroncraft.sce;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;

public class ServerReseter
{
    private final boolean useCommand;

    private final String fileName = "reset-required";

    private final String resetCommand;
    private boolean resetting;

    public ServerReseter(String resetCommand)
    {
        this.resetCommand = resetCommand;
        this.useCommand = resetCommand != null && !resetCommand.isEmpty();
        this.resetting = false;
    }

    public boolean getResetFlag()
    {
        return useCommand ? resetting : new File(fileName).exists();
    }

    public boolean resetFlag()
    {
        return useCommand ? resetWithCommand() : resetWithFile();
    }

    private boolean resetWithCommand()
    {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), resetCommand);
        resetting = true;
        return true;
    }

    private boolean resetWithFile()
    {
        File resetFile = new File(fileName);
        if (resetFile.exists())
            return true;

        try
        {
            return resetFile.createNewFile();
        } 
        catch(IOException e)
        {
            return false;
        }
    }
}
