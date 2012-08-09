package com.renderjunkies.noh.job;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class Cleric extends Job
{
	private static Cleric instance = null;
	
	private Cleric()
	{
		super();
		weapons = new ArrayList<Material>();
		weapons.add(Material.WOOD_SPADE);
		weapons.add(Material.STONE_SPADE);
		weapons.add(Material.IRON_SPADE);
		weapons.add(Material.DIAMOND_SPADE);
		helmetMaterial = Material.CHAINMAIL_HELMET;
		chestplateMaterial = Material.CHAINMAIL_CHESTPLATE;
		leggingsMaterial = Material.CHAINMAIL_LEGGINGS;
		bootsMaterial = Material.CHAINMAIL_BOOTS;
		baseOffense = 2.0f;
		baseDefense = 0.55f;
		name = "Cleric";
	}

	public static Cleric getInstance()
	{
		if(instance == null)
		{
			instance = new Cleric();
		}
		return instance;
	}

	@Override
	void UseJobTool(Player player, Material mat, Action act) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	void UseJobWeapon(Player player, Material mat, Action act) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public int TakeDamage(Player player, float damage) {
		// TODO Auto-generated method stub
		return (int)damage;
	}

	@Override
	public int DealDamage(Player player, LivingEntity enemy, float damage) {
		// TODO Auto-generated method stub
		return (int)damage;
	}

	@Override
	public void Update(Player player) {
		// TODO Auto-generated method stub
		
	}
	
}