package com.renderjunkies.noh;

import org.bukkit.entity.Player;

public enum EnumJobs
{
		RANGER, CLERIC, BERSERKER, SWORDSMAN;

		public static EnumJobs getJob(Player player)
		{
			int helmetId = player.getInventory().getHelmet().getTypeId();
			int chestId = player.getInventory().getChestplate().getTypeId();
			int legId = player.getInventory().getLeggings().getTypeId();
			int bootId = player.getInventory().getBoots().getTypeId();
			
			if(helmetId == 298 && chestId == 299 && legId == 300 && bootId == 301)
				return EnumJobs.RANGER;
			if(helmetId == 302 && chestId == 303 && legId == 304 && bootId == 305)
				return EnumJobs.CLERIC;
			if(helmetId == 306 && chestId == 307 && legId == 308 && bootId == 309)
				return EnumJobs.BERSERKER;
			if(helmetId == 310 && chestId == 311 && legId == 312 && bootId == 313)
				return EnumJobs.SWORDSMAN;
			else
				return null;
		}
}

