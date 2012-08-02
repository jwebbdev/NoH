package com.renderjunkies.noh;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.renderjunkies.noh.EnumJobs;

public class ExpManager
{
	NoH _plugin = null;
	Map<EntityType, Integer> ExpMap;

	public class PlayerData 
	{
		Map<EnumJobs, Integer> Experience;
		
		public PlayerData()
		{
			Experience = new HashMap<EnumJobs, Integer>();
			Experience.put(EnumJobs.RANGER, 0);
			Experience.put(EnumJobs.CLERIC, 0);
			Experience.put(EnumJobs.BERSERKER, 0);
			Experience.put(EnumJobs.SWORDSMAN, 0);
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
		EnumJobs pClass = EnumJobs.getJob(player);
		GrantExp(player, pClass, amount);
	}
	
	private void GrantExp(Player player, EnumJobs pClass, int amount)
	{
		if(!playerExp.containsKey(player.getName()))
			playerExp.put(player.getName(), new PlayerData());
		
		Map<EnumJobs, Integer> pExp = playerExp.get(player.getName()).Experience;
		pExp.put(pClass, pExp.get(pClass)+ amount);
		
		player.sendMessage("You just gained "+amount+"XP now you have "+GetExp(player, pClass));
	}
	
	public int GetExp(Player player, EnumJobs pClass)
	{
		if(playerExp.containsKey(player.getName()))
			return playerExp.get(player.getName()).Experience.get(pClass);
		return 0;
	}
}