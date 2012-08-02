package com.renderjunkies.noh.job;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.block.Action;

public abstract class Job
{
	protected Material weaponMaterial;
	protected Material helmetMaterial;
	protected Material chestplateMaterial;
	protected Material leggingsMaterial;
	protected Material bootsMaterial;
	protected float baseOffense;
	protected float baseDefense;
	protected String name;
	protected List<Material> toolList;
	
	abstract void UseJobTool(Material mat, Action act);
	abstract void UseJobWeapon(Action act);
	public boolean IsJobTool(Material mat)
	{
		if(toolList != null && toolList.contains(mat))
			return true;
		return false;
	}
	
	public boolean IsJobWeapon(Material mat)
	{
		if(weaponMaterial == mat)
			return true;
		return false;
	}
}