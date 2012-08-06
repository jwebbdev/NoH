package com.renderjunkies.noh.job;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
		if(event.getItem() == null)
			return;

		Action act = event.getAction();
		Material mat = event.getItem().getType();
		Map<String,Job> pJobs = _plugin.getPlayerJobMap();
		Player player = event.getPlayer();
		if(pJobs.containsKey(player.getName()))
		{
			if(pJobs.get(player.getName()).IsJobTool(mat))
				pJobs.get(player.getName()).UseJobTool(player, mat, act);
			else if (pJobs.get(player.getName()).IsJobWeapon(mat))
				pJobs.get(player.getName()).UseJobWeapon(player, mat, act);		
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event)
	{
		if(event instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent edee = (EntityDamageByEntityEvent)event;
			if(edee.getDamager() instanceof Player)
			{
				Player damager = (Player) edee.getDamager();
				damager.sendMessage(event.getCause().name());
				if(!event.isCancelled())
					if(_plugin.getPlayerJobMap().containsKey(damager.getName()) && _plugin.getPlayerJobMap().get(damager.getName()) != null)
						event.setDamage(_plugin.getPlayerJobMap().get(damager.getName()).DealDamage(damager, (LivingEntity)edee.getEntity(), event.getDamage()));
			}

			if(edee.getEntity() instanceof Player)
			{
				Player receiver = (Player) edee.getEntity();
				if(!event.isCancelled())
					if(_plugin.getPlayerJobMap().containsKey(receiver.getName()) && _plugin.getPlayerJobMap().get(receiver.getName()) != null)
						event.setDamage(_plugin.getPlayerJobMap().get(receiver.getName()).TakeDamage(receiver, event.getDamage()));
			}
		}
		else
			return;
	}
}