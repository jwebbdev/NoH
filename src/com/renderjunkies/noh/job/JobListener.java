package com.renderjunkies.noh.job;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.renderjunkies.noh.NoH;

public class JobListener implements Listener
{
	private NoH _plugin;
	public JobListener(NoH plugin)
	{
		_plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteraction(PlayerInteractEvent event)
	{
		if(!event.isBlockInHand())
			return;

		Action act = event.getAction();
		Material mat = event.getItem().getType();
		Map<Player,Job> pJobs = _plugin.getPlayerJobMap();
		Player player = event.getPlayer();
		if(pJobs.containsKey(player))
		{
			if(pJobs.get(player).IsJobTool(mat))
				pJobs.get(player).UseJobTool(mat, act);
			else if (pJobs.get(player).IsJobWeapon(mat))
				pJobs.get(player).UseJobWeapon(act);				
		}
	}
}