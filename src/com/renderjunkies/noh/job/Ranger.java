package com.renderjunkies.noh.job;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class Ranger extends Job
{
	private static Ranger instance = null;
	
	private Ranger()
	{
		super();
		weapons = new ArrayList<Material>();
		weapons.add(Material.BOW);
		helmetMaterial = Material.LEATHER_HELMET;
		chestplateMaterial = Material.LEATHER_CHESTPLATE;
		leggingsMaterial = Material.LEATHER_LEGGINGS;
		bootsMaterial = Material.LEATHER_BOOTS;
		baseOffense = 2.0f;
		baseDefense = 0.5f;
		name = "Ranger";
		List<Material> toolList = new ArrayList<Material>();
		toolList.add(Material.ARROW);
	}

	public static Ranger getInstance()
	{
		if(instance == null)
		{
			instance = new Ranger();
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