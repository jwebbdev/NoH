package com.renderjunkies.noh.job;

import java.util.ArrayList;

import org.bukkit.Material;
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
	void UseJobTool(Material mat, Action act) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	void UseJobWeapon(Action act) 
	{
		// TODO Auto-generated method stub
		
	}
	
}