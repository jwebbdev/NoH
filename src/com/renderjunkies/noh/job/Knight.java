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
		
		if(p.P())
		{
			player.sendMessage("Blocking");
			damage = 0;
		}
		
		return damage;
	}

	@Override
	public int DealDamage(Player player, int damage) {
		// TODO Auto-generated method stub
		return damage;
	}

	@Override
	public void Update(Player player) {
	}
	
}