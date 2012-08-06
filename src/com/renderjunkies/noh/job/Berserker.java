package com.renderjunkies.noh.job;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class Berserker extends Job
{
	private static Berserker instance = null;
	
	private Berserker()
	{
		super();
		weapons = new ArrayList<Material>();
		weapons.add(Material.WOOD_AXE);
		weapons.add(Material.STONE_AXE);
		weapons.add(Material.IRON_AXE);
		weapons.add(Material.DIAMOND_AXE);
		helmetMaterial = Material.IRON_HELMET;
		chestplateMaterial = Material.IRON_CHESTPLATE;
		leggingsMaterial = Material.IRON_LEGGINGS;
		bootsMaterial = Material.IRON_BOOTS;
		baseOffense = 3.0f;		
		baseDefense = 0.60f;
		name = "Berserker";
	}

	public static Berserker getInstance()
	{
		if(instance == null)
		{
			instance = new Berserker();
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
	public int TakeDamage(Player player, int damage) {
		// TODO Auto-generated method stub
		return damage;
	}

	@Override
	public int DealDamage(Player player, LivingEntity enemy, int damage) {
		// TODO Auto-generated method stub
		return damage;
	}

	@Override
	public void Update(Player player) {
		// TODO Auto-generated method stub
		
	}
	
}