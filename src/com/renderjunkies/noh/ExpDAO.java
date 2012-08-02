package com.renderjunkies.noh;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.renderjunkies.noh.ExpManager.PlayerData;
import com.renderjunkies.noh.EnumJobs;

import lib.PatPeter.SQLibrary.MySQL;

public class ExpDAO
{
	Map<EnumJobs, String> tableMap = null;
	public MySQL mysql = null;
	private MySQL _mysql = null;
	NoH _plugin = null;
	public ExpDAO(NoH plugin)
	{
		// Creating a new MySQL object so I don't have to worry about Saves blocking the main thread if it wants to do something else.
		this.mysql = new MySQL(plugin.getLogger(), plugin.getConfig().getString("database.prefix"),plugin.getConfig().getString("database.host"),plugin.getConfig().getString("database.port"),plugin.getConfig().getString("database.dbname"),plugin.getConfig().getString("database.username"),plugin.getConfig().getString("database.password"));
		this._mysql = new MySQL(plugin.getLogger(), plugin.getConfig().getString("database.prefix"),plugin.getConfig().getString("database.host"),plugin.getConfig().getString("database.port"),plugin.getConfig().getString("database.dbname"),plugin.getConfig().getString("database.username"),plugin.getConfig().getString("database.password"));
		this._plugin = plugin;

		tableMap = new HashMap<EnumJobs, String>();
		tableMap.put(EnumJobs.RANGER, "ranger");
		tableMap.put(EnumJobs.CLERIC, "cleric");
		tableMap.put(EnumJobs.BERSERKER, "berserker");
		tableMap.put(EnumJobs.SWORDSMAN, "swordsman");

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
		results = mysql.query("SELECT * from `noh_experience` WHERE name = '"+playerName+"'");
		try
		{
			if(!results.next())
			{
				// Player doesn't exist yet, need to create.
				mysql.query("INSERT INTO `noh_experience` (`name`) VALUES ('"+playerName+"')");
			}
		}
		catch (SQLException e) 
		{
			_plugin.getLogger().info("ERROR: Couldn't create player \""+playerName+"\".");
		}
		mysql.close();
	}
	
	// Only the SaveExp function uses _mysql, and it is synchronized so only calling SaveExp from the main thread should
	// cause blocking.
	public synchronized void SaveExp(ExpManager manager)
	{
		// TODO: Diff the changing ExpMap with the DB version and only commit changes
		// 		 on new saves, update the DB version
		this._mysql.open();
		List<String> existingPlayers = new ArrayList<String>();
		ResultSet results = this._mysql.query("SELECT name FROM `noh_experience`");
		try
		{
			while(results.next())
			{
				existingPlayers.add(results.getString("name"));
			}
		}
		catch (SQLException e) 
		{
			_plugin.getLogger().info("ERROR: In SaveExp");
		}
		
		for(Map.Entry<String, PlayerData> entry: manager.playerExp.entrySet())
		{
			if(!existingPlayers.contains(entry.getKey()))
			{
				this._mysql.query("INSERT INTO `noh_experience` (`name`) VALUES ('"+entry.getKey()+"')");
			}
			this._mysql.query("UPDATE `noh_experience` SET " +
					"`"+tableMap.get(EnumJobs.RANGER)+"` = "+entry.getValue().Experience.get(EnumJobs.RANGER)+", " +
					"`"+tableMap.get(EnumJobs.CLERIC)+"` = "+entry.getValue().Experience.get(EnumJobs.CLERIC)+", " +
					"`"+tableMap.get(EnumJobs.BERSERKER)+"` = "+entry.getValue().Experience.get(EnumJobs.BERSERKER)+", " +
					"`"+tableMap.get(EnumJobs.SWORDSMAN)+"` = "+entry.getValue().Experience.get(EnumJobs.SWORDSMAN)+" " +
					"WHERE name = '"+entry.getKey()+"'");
		}
		this._mysql.close();
		// Save Manager's ExpMap to Database
	}
	
	public void LoadExp(ExpManager manager)
	{
		// TODO: Load 2 versions of the map a DB version for diff and one that will be changed in runtime
		// Load Manager's ExpMap from Database
		mysql.open();
		ResultSet results = mysql.query("SELECT * from `noh_experience`");
		try
		{
			while(results.next())
			{
				if(!manager.playerExp.containsKey(results.getString("name")))
					manager.playerExp.put(results.getString("name"), manager.new PlayerData());
				for(EnumJobs pClass : EnumJobs.values())
					manager.playerExp.get(results.getString("name")).Experience.put(pClass, results.getInt(tableMap.get(pClass)));
			}
		}
		catch (SQLException e) 
		{
			_plugin.getLogger().info("ERROR: In SaveExp");
		}
		mysql.close();
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
			ResultSet results = mysql.query("CREATE TABLE `noh_experience` (`id` int PRIMARY KEY AUTO_INCREMENT, `name` VARCHAR(16), `ranger` int default 0, `cleric` int default 0, `berserker` int default 0, `swordsman` int default 0)");
			if(!results.wasNull())
			{
				_plugin.getLogger().info("First run: Experience Table Created Successfully.");
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