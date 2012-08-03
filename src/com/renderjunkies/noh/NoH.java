package com.renderjunkies.noh;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.renderjunkies.noh.job.Job;
import com.renderjunkies.noh.job.JobListener;

public class NoH extends JavaPlugin
{
	private ExpDAO ExpDao = null; 
	private ExpManager ExpMan = null;
	public final NoH noh = this;
	private Map<Player, Job> pJobs;
	
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new ExpListener(this),  this);
		getServer().getPluginManager().registerEvents(new JobListener(this),  this);
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		this.reloadConfig();
		
		ExpDao = new ExpDAO(this);
		ExpMan = new ExpManager(this);
		ExpDao.LoadExp(ExpMan);

		// Run Save every 5 minutes
		getServer().getScheduler().scheduleAsyncDelayedTask(noh,  new Runnable()
		{
			public void run()
			{
				noh.ExpDao.SaveExp(ExpMan);
			}
		}, 6000L);
		pJobs = new HashMap<Player, Job>();
	}
	
	public Map<Player, Job> getPlayerJobMap()
	{
		return pJobs;
	}
	
	public void onDisable()
	{
		ExpDao.SaveExp(ExpMan);
	}
	
	public ExpDAO getDao()
	{
		return this.ExpDao;
	}
	
	public ExpManager getExpManager()
	{
		return this.ExpMan;
	}	
}