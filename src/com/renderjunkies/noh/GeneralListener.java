package com.renderjunkies.noh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GeneralListener implements Listener
{
	Map<String, List<ItemStack>> dropMap;
	NoH plugin;
	
	public GeneralListener(NoH plugin)
	{
		this.plugin = plugin;
		dropMap = new HashMap<String, List<ItemStack>>();
	}
	
	
	// Store the player's equipment on death, but drop their loot
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();

		List<ItemStack> drops = event.getDrops();
		List<ItemStack> gear = new ArrayList<ItemStack>();
		
		List<Material> gearIds = new ArrayList<Material>();
		
		gearIds.add(Material.WOOD_SWORD);
		gearIds.add(Material.STONE_SWORD);
		gearIds.add(Material.IRON_SWORD);
		gearIds.add(Material.DIAMOND_SWORD);
		gearIds.add(Material.GOLD_SWORD);
		gearIds.add(Material.LEATHER_HELMET);
		gearIds.add(Material.LEATHER_CHESTPLATE);
		gearIds.add(Material.LEATHER_LEGGINGS);
		gearIds.add(Material.LEATHER_BOOTS);
		gearIds.add(Material.CHAINMAIL_HELMET);
		gearIds.add(Material.CHAINMAIL_CHESTPLATE);
		gearIds.add(Material.CHAINMAIL_LEGGINGS);
		gearIds.add(Material.CHAINMAIL_BOOTS);
		gearIds.add(Material.IRON_HELMET);
		gearIds.add(Material.IRON_CHESTPLATE);
		gearIds.add(Material.IRON_LEGGINGS);
		gearIds.add(Material.IRON_BOOTS);
		gearIds.add(Material.DIAMOND_HELMET);
		gearIds.add(Material.DIAMOND_CHESTPLATE);
		gearIds.add(Material.DIAMOND_LEGGINGS);
		gearIds.add(Material.DIAMOND_BOOTS);
		gearIds.add(Material.GOLD_HELMET);
		gearIds.add(Material.GOLD_CHESTPLATE);
		gearIds.add(Material.GOLD_LEGGINGS);
		gearIds.add(Material.GOLD_BOOTS);
		gearIds.add(Material.BOW);
		
		for(ItemStack stack : drops)
		{
			if(gearIds.contains(stack.getType()))
				gear.add(stack);
		}
		for(ItemStack stack : gear)
		{
			short maxDur = stack.getType().getMaxDurability();
			short curDur = stack.getDurability();
			double newDur = ((float) maxDur) * 0.10;
			newDur = curDur + newDur;
			stack.setDurability((short)newDur);
			drops.remove(stack);
		}
		dropMap.put(player.getName(), gear);
	}	
	
	// Restore the player's gear
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		Player player = event.getPlayer();
		if(dropMap.containsKey(player.getName()))
		{
			Inventory pInv = player.getInventory();
			for(ItemStack iStack : dropMap.get(player.getName()))
			{
				pInv.addItem(iStack);
				plugin.dOver.removeItem(iStack);
			}
			dropMap.remove(player.getName());
			player.sendMessage("You made it back to town, but your gear has been damaged and you dropped your loot.");
		}
	}
	
	// For DurabilityOverrides
	@EventHandler(priority = EventPriority.MONITOR)
	public void onEntityDamageEvent(EntityDamageEvent e)
	{
		if(e.getEntity() instanceof Player)
		{
			Player p = (Player)e.getEntity();
			for(ItemStack i : p.getInventory().getArmorContents())
			{
				plugin.dOver.adjustDurability(i, e);
			}
		}
		if(e instanceof EntityDamageByEntityEvent)
		{
			if(((EntityDamageByEntityEvent)e).getDamager() instanceof Player)
			{
				Player p = (Player)((EntityDamageByEntityEvent)e).getDamager();
				if(p.getItemInHand() != null && p.getItemInHand().getType().getMaxDurability() != 0)
				{
					plugin.dOver.adjustDurability(p.getItemInHand(), e);
				}
				else if (p.getItemInHand() != null)
				{
					plugin.getLogger().info("Item's Durability is "+p.getItemInHand().getType().getMaxDurability());
				}
			}
		}
	}
	
	
}