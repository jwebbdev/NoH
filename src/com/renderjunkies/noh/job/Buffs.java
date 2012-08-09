package com.renderjunkies.noh.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Buffs
{
	private static Buffs instance = null;
	private static long buffTimer = 0;
	public class Buff
	{
		String 	playerName;
		String 	buffName;
		int		duration;
		long	started;
		Buff(Player player, String buffName, int duration)
		{
			this.playerName = player.getName();
			this.buffName = buffName;
			this.duration = duration;
			this.started = buffTimer;
		}
	}
	
	private Map<String, List<Buff>> playerBuffs;
	
	private Buffs()
	{
		playerBuffs = new HashMap<String, List<Buff>>();
	}
	
	public static Buffs getInstance()
	{
		if(instance == null)
			instance = new Buffs();
		return instance;
	}
	
	public void Update(Plugin plugin)
	{
		buffTimer++;
		Map<String, List<Buff>> removal = new HashMap<String, List<Buff>>();
		
		for(Map.Entry<String, List<Buff>> entry: playerBuffs.entrySet())
		{
			// Skip offline players
			if(plugin.getServer().getPlayer(entry.getKey()) == null || !plugin.getServer().getPlayer(entry.getKey()).isOnline())
				continue;
			
			for(Buff b : entry.getValue())
			{
				// If the buff has finished its duration, add it to the removal list.
				if(buffTimer > b.duration + b.started)
				{
					if(!removal.containsKey(entry.getKey()))
						removal.put(entry.getKey(), new ArrayList<Buff>());
					removal.get(entry.getKey()).add(b);
					plugin.getServer().getPlayer(entry.getKey()).sendMessage(b.buffName+" has worn off.");
				}
			}
		}
		
		// Remove everything in the removal map
		for(Map.Entry<String, List<Buff>> entry : removal.entrySet())
		{
			for(Buff b : entry.getValue())
				playerBuffs.get(entry.getKey()).remove(b);
			
			if(playerBuffs.get(entry.getKey()).isEmpty())
				playerBuffs.remove(entry.getKey());
		}
	}
	
	public void AddBuff(Player player, String buffName, int duration)
	{
		if(!playerBuffs.containsKey(player.getName()))
			playerBuffs.put(player.getName(), new ArrayList<Buff>());
		
		playerBuffs.get(player.getName()).add(new Buff(player, buffName, duration/2));
		player.sendMessage(buffName+" has been applied to you.");
	}
	
	public float DealDamage(Player player, float amount)
	{
		return amount;
	}

	// Buffs processed here modify incoming damage
	public float TakeDamage(Player player, float amount)
	{
		if(playerBuffs.containsKey(player.getName()))
		{
			List<Buff> pBuffs = playerBuffs.get(player.getName());
			if(pBuffs != null)
			{
				for(Buff b : pBuffs)
				{
					// Immovable Object reduces damage by 10%
					if(b.buffName == "Immovable Object")
					{
						amount *= 0.10f;
						break;
					}
				}
			}
		}
		return amount;
	}
	
	public int UpdatePower(Player player, int power)
	{
		return power;
	}
}