package com.renderjunkies.noh.job;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.block.Action;

public class Knight extends Job
{
	private static Knight instance = null;
	
	private Knight()
	{
		weapons = new ArrayList<Material>();
		weapons.add(Material.WOOD_SWORD);
		weapons.add(Material.STONE_SWORD);
		weapons.add(Material.IRON_SWORD);
		weapons.add(Material.DIAMOND_SWORD);
		helmetMaterial = Material.LEATHER_HELMET;
		chestplateMaterial = Material.LEATHER_CHESTPLATE;
		leggingsMaterial = Material.LEATHER_LEGGINGS;
		bootsMaterial = Material.LEATHER_BOOTS;
		baseOffense = 1.0f;
		baseDefense = 1.0f;
		name = "Knight";
		List<Material> toolList = new ArrayList<Material>();
		toolList.add(Material.ARROW);
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
	void UseJobTool(Material mat, Action act) {
		// TODO Auto-generated method stub
		
	}

	@Override
	void UseJobWeapon(Action act) {
		// TODO Auto-generated method stub
		
	}
	
}