package com.barroncraft.sce;

import java.io.File;
import java.io.IOException;

public class ServerReloader 
{
	public static boolean SetReloadFlag(boolean reset)
	{
		File resetFile = new File("reset-required");
		boolean fileExists = resetFile.exists();
		try
		{
			return 
			(
				reset && !fileExists && 
				resetFile.createNewFile()
			) 
			||
			(
				!reset && fileExists && 
				resetFile.delete()
			);
		} 
		catch(IOException e)
		{
			return false;
		}
	}
}
