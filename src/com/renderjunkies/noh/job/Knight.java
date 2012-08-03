package com.renderjunkies.noh.job;

import java.util.ArrayList;

import net.minecraft.server.EntityPlayer;

import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class Knight extends Job
{
	private static Knight instance = null;
	
	private Knight()
	{
		super();
		weapons = new ArrayList<Material>();
		weapons.add(Material.WOOD_SWORD);
		weapons.add(Material.STONE_SWORD);
		weapons.add(Material.IRON_SWORD);
		weapons.add(Material.DIAMOND_SWORD);
		helmetMaterial = Material.DIAMOND_HELMET;
		chestplateMaterial = Material.DIAMOND_CHESTPLATE;
		leggingsMaterial = Material.DIAMOND_LEGGINGS;
		bootsMaterial = Material.DIAMOND_BOOTS;
		baseOffense = 2.0f;
		baseDefense = 0.75f;
		maxPower = 100;
		name = "Knight";
	}

	public static Knight getInstance()
	{
		if(instance == null)
		{
			instance = new Knight();
		}
		return instance;
	}

	@Override
	void UseJobTool(Player player, Material mat, Action act) 
	{
		player.sendMessage("Used Job Tool");
	}

	@Override
	void UseJobWeapon(Player player, Material mat, Action act) 
	{
		player.sendMessage("Used Job Weapon");
		if(act == Action.RIGHT_CLICK_AIR || act == Action.RIGHT_CLICK_BLOCK)
		{
			// Player is blocking
		}
	}
	
	@Override
	public int TakeDamage(Player player, int damage)
	{
		// Blocking
		EntityPlayer p = ((CraftPlayer)player).getHandle();
		
		// Does the player have enough stamina to block?
		int staminaNeeded = (int)((float)damage * BlockResistFactor(player));
		
		if(player.getLevel() >= staminaNeeded)
		{
			// Player has enough to block, see if they're blocking
			if(p.P())
			{
				player.setLevel(player.getLevel() - staminaNeeded);
				damage = 0;
			}
		}
		
		return damage;
	}

	@Override
	public int DealDamage(Player player, int damage) 
	{
		// TODO Auto-generated method stub
		return damage;
	}

	@Override
	public void Update(Player player) 
	{
		if(player.getLevel() < maxPower)
		{
			int curLevel = player.getLevel();
			curLevel += GetIncrement(player);
			if(curLevel > maxPower)
				curLevel = maxPower;
			player.setLevel(curLevel);
		}
	}
	
	public int GetIncrement(Player player)
	{
		return 2;
	}
	
	public float BlockResistFactor(Player player)
	{
		// Figure out Player's level and determine how much damage they soak from that
		// 1.0 = 1 stamina for 1 damage
		return 5.0f;
	}
	
}