package com.renderjunkies.noh;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import com.renderjunkies.noh.job.Job;
import com.renderjunkies.noh.job.JobCommands;
import com.renderjunkies.noh.job.JobDAO;
import com.renderjunkies.noh.job.JobListener;

public class NoH extends JavaPlugin
{
	private ExpDAO ExpDao = null; 
	private JobDAO JobDao = null; 
	private ExpManager ExpMan = null;
	public final NoH noh = this;
	private Map<String, Job> pJobs;
	private JobCommands jCommands;
	
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new ExpListener(this),  this);
		getServer().getPluginManager().registerEvents(new JobListener(this),  this);
		jCommands = new JobCommands(this);
		getCommand("knight").setExecutor(jCommands);
		
		
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		this.reloadConfig();
		
		ExpDao = new ExpDAO(this);
		ExpMan = new ExpManager(this);
		ExpDao.LoadExp(ExpMan);

		// Create the Job Hashmap, then load it up from the database
		JobDao = new JobDAO(this);

		pJobs = new HashMap<String, Job>();
		JobDao.LoadJobs();

		// Run Save every 5 minutes
		getServer().getScheduler().scheduleAsyncDelayedTask(noh,  new Runnable()
		{
			public void run()
			{
				noh.ExpDao.SaveExp(ExpMan);
				noh.JobDao.SaveJobs();
			}
		}, 6000L);
	}
	
	public Map<String, Job> getPlayerJobMap()
	{
		return pJobs;
	}
	
	public void onDisable()
	{
		ExpDao.SaveExp(ExpMan);
		JobDao.SaveJobs();
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