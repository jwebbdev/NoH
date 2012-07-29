package com.renderjunkies.noh.classes;

import java.sql.ResultSet;
import java.sql.SQLException;

import lib.PatPeter.SQLibrary.MySQL;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Classes extends JavaPlugin
{
	public enum PlayerClass
	{
		BASE, RANGER, CLERIC, BERSERKER, SWORDSMAN;
	}
	
	private MySQL mysql = null;
	private ExpDAO ExpDao = null; 
	private ExpManager ExpMan = null;
	public final Classes thisClasses = this;
	
	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(new ExpListener(this),  this);
		mysql = new MySQL(getLogger(), getConfig().getString("database.prefix"),getConfig().getString("database.host"),getConfig().getString("database.port"),getConfig().getString("database.dbname"),getConfig().getString("database.username"),getConfig().getString("database.password"));

		mysql.open();
		if(mysql.checkConnection())
		{
			getLogger().info("Database connection established.");
			CheckTables();
		}
		else
		{
			getLogger().info("Database connection could not be established.");
		}
		mysql.close();
		
		ExpDao = new ExpDAO(this);
		ExpMan = new ExpManager(this);
		ExpDao.LoadExp(ExpMan);
		
		// Run Save every 5 minutes
		getServer().getScheduler().scheduleAsyncDelayedTask(thisClasses,  new Runnable()
		{
			public void run()
			{
				thisClasses.ExpDao.SaveExp(ExpMan);
			}
		}, 6000L);
	}
	
	public void onDisable()
	{
		ExpDao.SaveExp(ExpMan);
	}
	
	public MySQL getMySQL()
	{
		return this.mysql;
	}
	
	public ExpDAO getDao()
	{
		return this.ExpDao;
	}
	
	public ExpManager getExpManager()
	{
		return this.ExpMan;
	}

	public boolean CheckTables()
	{
		if(mysql.checkTable("noh_experience"))
			return true;
		else if(CreateTables())
			return true;
		return false;
	}
	
	public boolean CreateTables()
	{
		try
		{
			ResultSet results = mysql.query("CREATE TABLE `noh_experience` (`id` int PRIMARY KEY AUTO_INCREMENT, `name` VARCHAR(16), `base` int default 0, `ranger` int default 0, `cleric` int default 0, `berserker` int default 0, `swordsman` int default 0)");
			if(!results.wasNull())
			{
				getLogger().info("First run: Experience Table Created Successfully.");
				return true;
			}
		}
		catch (SQLException e) 
		{
	        //success = false;
	    	getLogger().info("SQLException: " + e.getMessage());
	        //e.printStackTrace();
	    	return false;
		}
		return false;
	}
	
	public PlayerClass getClass(Player player)
	{
		// TODO: Parse player's equipment to determine class.
		return PlayerClass.BASE;
	}
	
}