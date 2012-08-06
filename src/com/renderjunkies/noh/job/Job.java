package com.renderjunkies.noh.job;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public abstract class Job
{
	protected List<Material> weapons;
	protected Material helmetMaterial;
	protected Material chestplateMaterial;
	protected Material leggingsMaterial;
	protected Material bootsMaterial;
	protected float baseOffense;
	protected float baseDefense;
	protected String name;
	protected List<Material> toolList;
	protected int maxPower;
	
	Job()
	{
		toolList = null;
		weapons = null;
	}
	
	abstract void UseJobTool(Player player, Material mat, Action act);
	abstract void UseJobWeapon(Player player, Material mat, Action act);
	public boolean IsJobTool(Material mat)
	{
		if(toolList != null && toolList.contains(mat))
			return true;
		return false;
	}
	
	public boolean IsJobWeapon(Material mat)
	{
		if(weapons.contains(mat))
			return true;
		return false;
	}
	
	public String getName()
	{
		return name;
	}
	
	abstract public int TakeDamage(Player player, int damage);
	abstract public int DealDamage(Player player, LivingEntity enemy, int damage);
	abstract public void Update(Player player);
}