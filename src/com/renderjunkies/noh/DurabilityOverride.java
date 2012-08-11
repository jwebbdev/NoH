package com.renderjunkies.noh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class DurabilityOverride
{
	NoH plugin;
	
	private class DurabilityData
	{
		public float maxDurability;
		public float curDurability;
		
		DurabilityData(float m, float c)
		{
			maxDurability = m;
			curDurability = c;
		}
	}
	
	Map<ItemStack, DurabilityData> durMap;
	List<Material> weaponList;
	
	DurabilityOverride(NoH plugin)
	{
		this.plugin = plugin;
		durMap = new HashMap<ItemStack, DurabilityData>();
		weaponList = new ArrayList<Material>();
		weaponList.add(Material.WOOD_SWORD);
		weaponList.add(Material.IRON_SWORD);
		weaponList.add(Material.GOLD_SWORD);
		weaponList.add(Material.STONE_SWORD);
		weaponList.add(Material.DIAMOND_SWORD);
	}
	
	public void adjustDurability(ItemStack item, Event event)
	{
		// If it's not defined, just do normal stuff.
		if(getMaxDur(item) == -1.0f)
			return;
		
		int durChange = 0;
		if(event instanceof EntityDamageEvent)
		{
			if(((EntityDamageEvent) event).getDamage() == 0 || ((EntityDamageEvent) event).isCancelled())
				return;
			if(isTool(item))
			{
				LivingEntity thing = (LivingEntity)((EntityDamageEvent) event).getEntity();
				if(thing.getNoDamageTicks() > thing.getMaximumNoDamageTicks() / 2)
					return;
				durChange = 1;
			}
			else
				durChange = ((EntityDamageEvent) event).getDamage();
		}

		if(!durMap.containsKey(item))
		{
			float itemMaxDur = getMaxDur(item);
			float durMod = (float)item.getDurability() / (float)item.getType().getMaxDurability();
			durMap.put(item, new DurabilityData(itemMaxDur, itemMaxDur * durMod));
		}
		
		DurabilityData ddata = durMap.get(item);
		
		// If the durability of the item went down, that means it got repaired and we need to reset
		ddata.curDurability += durChange;
		plugin.getLogger().info(""+ddata.curDurability);
		
		int newDur = (int)((ddata.curDurability /ddata.maxDurability) * item.getType().getMaxDurability());
		newDur = newDur - durChange;
		
		// Don't want the item to keep breaking client side until it's ready to break for real.
		if(ddata.curDurability >= ddata.maxDurability)
			newDur = item.getType().getMaxDurability();
		else if(newDur + durChange >= item.getType().getMaxDurability() - 2)
			newDur -= 1;
		item.setDurability((short)newDur);
	}
	
	public float getMaxDur(ItemStack item)
	{
		float maxDur = -1.0f;
		
		switch(item.getType())
		{
		case WOOD_SWORD:
		case STONE_SWORD:
		case IRON_SWORD:
		case GOLD_SWORD:
		case DIAMOND_SWORD:
		case WOOD_AXE:
		case STONE_AXE:
		case IRON_AXE:
		case GOLD_AXE:
		case DIAMOND_AXE:
		case BOW:
			maxDur = 1000.0f;
			break;
		default:
			break;
		}
		return maxDur;
	}
	
	public boolean isTool(ItemStack item)
	{
		if(weaponList.contains(item.getType()))
			return true;
		return false;
	}
	
	public void removeItem(ItemStack item)
	{
		if(durMap.containsKey(item))
			durMap.remove(item);
	}
}