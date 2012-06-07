package com.barroncraft.sce;

import java.io.File;
import java.io.IOException;

public class ServerReloader 
{
	public static boolean FlagForReload()
	{
		File resetFile = new File("reset-required");
		if (!resetFile.exists())
		{
			try
			{
				resetFile.createNewFile();
			} 
			catch(IOException e)
			{
				return false;
			}
		}
		return true;
	}
}
