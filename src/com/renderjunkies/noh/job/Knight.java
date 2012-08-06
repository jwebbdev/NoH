package com.renderjunkies.noh.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.server.EntityPlayer;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class Knight extends Job
{
	private static Knight instance = null;
	private Map<String, Long> blockCooldown;
	private Map<LivingEntity, DoT> bleeding;
	private class DoT
	{
		public int duration;
		public int damage;
		public long started;
		Player player;
		DoT(Player player, int duration, int damage)
		{
			this.player = player;
			this.duration = duration;
			this.damage = damage;
			this.started = player.getWorld().getFullTime();
		}
	}
	
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
		maxPower = 100;
		name = "Knight";
		blockCooldown = new HashMap<String, Long>();
		bleeding = new HashMap<LivingEntity, DoT>();
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
		//player.sendMessage("Used Job Tool");
	}

	@Override
	void UseJobWeapon(Player player, Material mat, Action act) 
	{
		//player.sendMessage("Used Job Weapon");
		if(act == Action.RIGHT_CLICK_AIR || act == Action.RIGHT_CLICK_BLOCK)
		{
			// Player is blocking
		}
	}
	
	@Override
	public int TakeDamage(Player player, int damage)
	{
		EntityPlayer p = ((CraftPlayer)player).getHandle();
		
		// Blocking
		if(p.P())
		{
			damage = BlockDamage(player, damage);
		}
		
		return damage;
	}
	
	private int BlockDamage(Player player, int damage)
	{
		if(blockCooldown.containsKey(player.getName()))
		{
			long timeLeft = (blockCooldown.get(player.getName()) + 200) - player.getWorld().getFullTime();
			if(timeLeft <= 0)
			{
				blockCooldown.remove(player.getName());
			}
			else
			{
				player.sendMessage("Defense Broken - You cannot block for another "+timeLeft / 20+" seconds.");
			}
		}

		// Rechecks to see if the player was removed.
		if (!blockCooldown.containsKey(player.getName()))
		{
			// Does the player have enough stamina to block?
			int staminaNeeded = (int)((float)damage * BlockResistFactor(player));
			
			if(player.getLevel() >= staminaNeeded)
			{
					player.setLevel(player.getLevel() - staminaNeeded);
					player.getWorld().playEffect(player.getLocation(), Effect.ZOMBIE_CHEW_IRON_DOOR, 0);
					damage = 0;
			}
			else
			{
				// Drain the rest of their stamina and let the appropriate damage through.
				if(player.getLevel() != 0)
				{
					int overflow = staminaNeeded - player.getLevel();
					player.setLevel(0);
					damage = (int)((float)overflow / BlockResistFactor(player));
					player.getWorld().playEffect(player.getLocation(), Effect.ZOMBIE_DESTROY_DOOR, 0);
					player.sendMessage("Your defenses have been broken, you must wait 10 seconds to block again.");
					blockCooldown.put(player.getName(), player.getWorld().getFullTime());
				}
			}
		}
		
		// Counter the effects of normal blocking by doubling damage, this will be cut back in half by the PlayerEntity class
		damage *= 2;
		
		return damage;
	}

	@Override
	public int DealDamage(Player player, LivingEntity enemy, int damage) 
	{
		// TODO Auto-generated method stub
		if(player.getItemInHand() != null && IsJobWeapon(player.getItemInHand().getType()))
		{
			Material mat = player.getItemInHand().getType();
			
			switch(mat)
			{
			case WOOD_SWORD:
				// Modified = 1.0, do nothing.
				break;
			case STONE_SWORD:
				// 2.5 -> 4
				damage *= 1.6f;
				break;
			case IRON_SWORD:
				// 3.0 -> 6
				damage *= 2.0f;
				break;
			case DIAMOND_SWORD:
				// 3.5 -> ~8
				damage *= 2.3f;
				break;
			default:
				break;
			}
		}
		
		// Gain some power as you deal damage
		// TODO make this a higher level passive
		if(damage > 0 && enemy.getNoDamageTicks() < enemy.getMaximumNoDamageTicks() / 2.0f)
		{
		 	 IncreasePower(player, GetIncrement(player));
		 	 // TODO make a way to differentiate between bleed and normal attack so it doesn't get weapon multipliers
		 	 // InflictDoT(player, enemy, 5, 10);
		}
		
		return damage;
	}
	
	public void IncreasePower(Player player, int amount)
	{
		int curLevel = player.getLevel();
		curLevel += amount;
		if(curLevel > maxPower)
			curLevel = maxPower;
		player.setLevel(curLevel);
	}
	
	public boolean CanUse(Player player, Material mat)
	{
		if(!IsJobTool(mat) || !IsJobWeapon(mat))
			return false;
		
		// Do some level based checks on items
		
		return true;
	}

	@Override
	public void Update(Player player) 
	{
		if(player.getLevel() < maxPower)
		{
			IncreasePower(player, GetIncrement(player));
		}
		
		// Happens every 2 seconds, so 40 ticks.
		List<LivingEntity> removal = new ArrayList<LivingEntity>(); 
		for(Map.Entry<LivingEntity, DoT> entry : bleeding.entrySet())
		{
			if(entry.getValue().player == player)
			{
				if(entry.getKey().isDead())
					removal.add(entry.getKey());
				else
				{
					entry.getKey().damage(entry.getValue().damage, entry.getValue().player);
					if(player.getWorld().getFullTime() > entry.getValue().duration + entry.getValue().started)
						removal.add(entry.getKey());
				}
			}
		}
		for(LivingEntity e : removal)
		{
			bleeding.remove(e);
		}
	}
	
	public void InflictDoT(Player player, LivingEntity enemy, int damagePer, int duration)
	{
		bleeding.put(enemy, new DoT(player, duration, damagePer));
	}
	
	public int GetIncrement(Player player)
	{
		return 2;
	}
	
	public float BlockResistFactor(Player player)
	{
		// Figure out Player's level and determine how much damage they soak from that
		// 1.0 = 1 stamina for 1 damage
		return 5.0f;
	}
	
}