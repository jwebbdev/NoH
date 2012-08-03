package com.renderjunkies.noh.job;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.renderjunkies.noh.NoH;

import lib.PatPeter.SQLibrary.MySQL;

public class JobDAO
{
	public MySQL mysql = null;
	private MySQL _mysql = null;
	NoH _plugin = null;
	public JobDAO(NoH plugin)
	{
		// Creating a new MySQL object so I don't have to worry about Saves blocking the main thread if it wants to do something else.
		this.mysql = new MySQL(plugin.getLogger(), plugin.getConfig().getString("database.prefix"),plugin.getConfig().getString("database.host"),plugin.getConfig().getString("database.port"),plugin.getConfig().getString("database.dbname"),plugin.getConfig().getString("database.username"),plugin.getConfig().getString("database.password"));
		this._mysql = new MySQL(plugin.getLogger(), plugin.getConfig().getString("database.prefix"),plugin.getConfig().getString("database.host"),plugin.getConfig().getString("database.port"),plugin.getConfig().getString("database.dbname"),plugin.getConfig().getString("database.username"),plugin.getConfig().getString("database.password"));
		this._plugin = plugin;

		mysql.open();
		if(mysql.checkConnection())
		{
			CheckTables();
		}
		else
		{
			_plugin.getLogger().info("Database connection could not be established.");
		}
		mysql.close();
		
	}
	
	public void PlayerExists(Player player)
	{
		String playerName = player.getName();
		
		ResultSet results;
		mysql.open();
		results = mysql.query("SELECT * from `noh_player_jobs` WHERE name = '"+playerName+"'");
		try
		{
			if(!results.next())
			{
				// Player doesn't exist yet, need to create.
				mysql.query("INSERT INTO `noh_player_jobs` (`name`, `job`) VALUES ('"+playerName+"', null)");
			}
		}
		catch (SQLException e) 
		{
			_plugin.getLogger().info("ERROR: JobDAO couldn't create player \""+playerName+"\".");
		}
		mysql.close();
	}
	
	// Only the SaveJobs function uses _mysql, and it is synchronized so only calling SaveJobs from the main thread should
	// cause blocking.
	public synchronized void SaveJobs()
	{
		// TODO: Diff the changing JobMap with the DB version and only commit changes
		// 		 on new saves, update the DB version
		this._mysql.open();
		List<String> existingPlayers = new ArrayList<String>();
		ResultSet results = this._mysql.query("SELECT name FROM `noh_player_jobs`");
		try
		{
			while(results.next())
			{
				existingPlayers.add(results.getString("name"));
			}
		}
		catch (SQLException e) 
		{
			_plugin.getLogger().info("ERROR: In SaveJob");
		}
		
		for(Map.Entry<String, Job> entry: _plugin.getPlayerJobMap().entrySet())
		{
			if(!existingPlayers.contains(entry.getKey()))
			{
				this._mysql.query("INSERT INTO `noh_player_jobs` (`name`) VALUES ('"+entry.getKey()+"')");
			}
			this._mysql.query("UPDATE `noh_player_jobs` SET `job` = '" + entry.getValue().getName() + "' WHERE name = '" +entry.getKey()+"'");
		}
		this._mysql.close();
	}
	
	public void LoadJobs()
	{
		// TODO: Load 2 versions of the map a DB version for diff and one that will be changed in runtime
		// Load Jobs from DB
		mysql.open();
		ResultSet results = mysql.query("SELECT * from `noh_player_jobs`");
		try
		{
			while(results.next())
			{
				switch(results.getString("job").toLowerCase())
				{
					case "knight":
						_plugin.getPlayerJobMap().put(results.getString("name"), Knight.getInstance());
						break;
					case "ranger":
						_plugin.getPlayerJobMap().put(results.getString("name"), Ranger.getInstance());
						break;
					case "cleric":
						_plugin.getPlayerJobMap().put(results.getString("name"), Cleric.getInstance());
						break;
					case "berserker":
						_plugin.getPlayerJobMap().put(results.getString("name"), Berserker.getInstance());
						break;
					default:
						// If job is null from creation and sitting at spawn, don't insert.
						break;
				}
			}
		}
		catch (SQLException e) 
		{
			_plugin.getLogger().info("ERROR: In SaveJob");
		}
		mysql.close();
	}

	public boolean CheckTables()
	{
		if(mysql.checkTable("noh_player_jobs"))
			return true;
		else if(CreateTables())
			return true;
		return false;
	}

	public boolean CreateTables()
	{
		try
		{
			ResultSet results = mysql.query("CREATE TABLE `noh_player_jobs` (`id` int PRIMARY KEY AUTO_INCREMENT, `name` VARCHAR(16) NOT NULL, `job` VARCHAR(16))");
			if(!results.wasNull())
			{
				_plugin.getLogger().info("First run: Job table created successfully.");
				return true;
			}
		}
		catch (SQLException e) 
		{
	        //success = false;
	    	_plugin.getLogger().info("SQLException: " + e.getMessage());
	        //e.printStackTrace();
	    	return false;
		}
		return false;
	}
}