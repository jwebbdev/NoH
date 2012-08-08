package com.renderjunkies.noh;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class ExpListener implements Listener
{
	private final NoH _plugin;
	public ExpListener(NoH plugin)
	{
		this._plugin = plugin;
	}
	
	@EventHandler
	public void onMonsterDeath(EntityDeathEvent event)
	{
		LivingEntity monster = event.getEntity();

		if(monster instanceof Player)
			return;
		
		Player killer = monster.getKiller();
		if(killer == null)
		{
			if(monster.getLastDamageCause() instanceof EntityDamageByEntityEvent)
			{
				Entity e = ((EntityDamageByEntityEvent)monster.getLastDamageCause()).getDamager();
				if(e instanceof Player)
					killer = (Player)e;
			}
		}
				
		if(killer instanceof Player)
		{
			_plugin.getExpManager().PlayerKill(killer, monster);
		}
		else
			return;
	}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		_plugin.getDao().PlayerExists(event.getPlayer());
	}

}