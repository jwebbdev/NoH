package com.renderjunkies.noh;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.renderjunkies.noh.job.Berserker;
import com.renderjunkies.noh.job.Cleric;
import com.renderjunkies.noh.job.Job;
import com.renderjunkies.noh.job.Knight;
import com.renderjunkies.noh.job.Ranger;

public class ExpManager
{
	NoH _plugin = null;
	Map<EntityType, Integer> ExpMap;
	
	// XP Values, 0 - 50
	int[] expValues = { 0,0,600,1300,2110,3040,4100,5300,6650,8160,9840,11700,13750,16000,18460,21140,24050,27200,30600,34260,38190,42400,46900,51700,56810,62240,68000,74100,80550,87360,94540,102100,110050,118400,127160,136340,145950,156000,166500,177460,188890,200800,213200,226100,239510,253440,267900,282900,298450,314560,331240 };

	public class PlayerData 
	{
		Map<String, Integer> Experience;
		
		public PlayerData()
		{
			Experience = new HashMap<String, Integer>();
			Experience.put(Ranger.getInstance().getName(), 0);
			Experience.put(Cleric.getInstance().getName(), 0);
			Experience.put(Berserker.getInstance().getName(), 0);
			Experience.put(Knight.getInstance().getName(), 0);
		}
	}
	
	Map<String, PlayerData> playerExp = null;

	public ExpManager(NoH plugin)
	{
		this._plugin = plugin;
		
		ExpMap = new HashMap<EntityType, Integer>();		
		
		// Move this to a config file
		ExpMap.put(EntityType.CREEPER, 3);
		ExpMap.put(EntityType.CAVE_SPIDER, 3);
		ExpMap.put(EntityType.SKELETON, 2);
		ExpMap.put(EntityType.SPIDER, 2);
		ExpMap.put(EntityType.ZOMBIE, 2);

		ExpMap.put(EntityType.GHAST, 5);
		ExpMap.put(EntityType.SILVERFISH, 3);
		
		playerExp = new HashMap<String, PlayerData>();
	}
	
	public void PlayerKill(Player player, LivingEntity monster)
	{
		EntityType monsterType = monster.getType();
		int amount = 0;
		if(ExpMap.containsKey(monsterType))
			amount = ExpMap.get(monster.getType());
		Job pJob = _plugin.getPlayerJobMap().get(player.getName());
		if(pJob != null)
			GrantExp(player, pJob.getName(), amount);
	}
	
	private void GrantExp(Player player, String pJob, int amount)
	{
		if(!playerExp.containsKey(player.getName()))
			playerExp.put(player.getName(), new PlayerData());
		
		Map<String, Integer> pExp = playerExp.get(player.getName()).Experience;
		pExp.put(pJob, pExp.get(pJob)+ amount);
		int curLevel = GetLevel(player);
		
		player.sendMessage(pJob + ": +"+amount+"XP ["+ (GetExp(player, pJob)-expValues[curLevel]) +" / "+ (expValues[curLevel+1] - expValues[curLevel]) +"] ("+curLevel+")");
	}
	
	public int GetExp(Player player, String pJob)
	{
		if(playerExp.containsKey(player.getName()))
			return playerExp.get(player.getName()).Experience.get(pJob);
		return 0;
	}
	
	public int GetLevel(Player player)
	{
		// Get the player's exp for their current job
		int exp = playerExp.get(player.getName()).Experience.get(_plugin.getPlayerJobMap().get(player.getName()).getName());
		
		for(int i = 0; i < expValues.length; i++)
		{
			if(expValues[i] > exp)
				return i-1;
		}
		return 50;
	}
}