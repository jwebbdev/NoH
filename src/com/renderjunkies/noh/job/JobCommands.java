package com.renderjunkies.noh.job;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.renderjunkies.noh.NoH;

public class JobCommands implements CommandExecutor
{
	private NoH _plugin;
	
	public JobCommands(NoH plugin)
	{
		_plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		Player player = null;
		if(sender instanceof Player)
			player = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("knight"))
		{
			if(player == null)
			{
				sender.sendMessage("This command can only be run by a player.");
			}
			else
			{
				_plugin.getPlayerJobMap().put(player.getName(), Knight.getInstance());
				player.sendMessage("You are now a Knight!");
			}
			return true;
		}
		
		return false;
	}
	
}