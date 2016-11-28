package com.obvious.Utils;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.obvious.McToTs;

public class MySQLUtil {
	
	private static Connection conn;
	private static Scanner s;
	private static Plugin pl = McToTs.getPlugin();
	
	public static void connect() 
	{
		String url = null;

		try 
		
		{
			
            Class.forName("com.mysql.jdbc.Driver");
        }
		
        catch (ClassNotFoundException e1) 
		
		{
        	
        	pl.getLogger().log(Level.SEVERE,ChatColor.DARK_RED + "Erreur lors de la liaison MySQL : logs dans le dossier error.log du plugin");
			savestacktrace(e1);
			McToTs.setDisable(true);
        }
		
        try 
        
        {
        	 url = "jdbc:mysql://"+pl.getConfig().getString("sql.host") + ":" + pl.getConfig().getInt("sql.port") + "/" + pl.getConfig().getString("sql.database")+"?autoReconnect=true";
            conn = DriverManager.getConnection(url, pl.getConfig().getString("sql.user"), pl.getConfig().getString("sql.passwd"));
            verify();

        }
        
        catch (SQLException e2) {
        	pl.getLogger().log(Level.SEVERE,ChatColor.DARK_RED + "Erreur lors de la liaison MySQL : logs dans le dossier error.log du plugin veuillez corriger les informations et redemarrer le serveur");
			savestacktrace(e2);
			McToTs.setDisable(true);
        }
		
	}
	
	public static void savestacktrace(Exception e2) {
		try {
			
			File file = new File(pl.getDataFolder(),"error.log");
			PrintStream ps = new PrintStream(file);
			e2.printStackTrace(ps);
			ps.close();
			
		} catch (Exception e) {
			pl.getLogger().log(Level.SEVERE,ChatColor.DARK_RED + "Erreur impossible de sauvegarder la stacktrace de l'erreur");
		}
	}
	
	public static void verify() {
		DatabaseMetaData meta = null;
		
		try {
			
			meta = conn.getMetaData();
			
		}
		
		catch (SQLException e) 
		
		{
			
			e.printStackTrace();
			
		}
		
		ResultSet res = null;
		
		try {
			
			res = meta.getTables(null, null, "joueur", null);
			
		} 
		
		catch (SQLException e) 
		
		{
			
			e.printStackTrace();
			
		}
		
		Statement state = null;
		
		try {
			
			if(!res.next())
				
			{
				
				pl.getLogger().log(Level.INFO, ChatColor.YELLOW + "Initialisation de la table MySQL");
				InputStream in = pl.getResource("table.sql");
				s = new Scanner(in);
			    s.useDelimiter("/\\*[\\s\\S]*?\\*/|--[^\\r\\n]*|;");
			    
			    try{
			    	 state = conn.createStatement();
			    	
			    	while(s.hasNext())
			    		
			    	{
			    		
			    		String line = s.next().trim();
			    		
			    		if(!line.isEmpty())
			    			state.execute(line);
			    		
			    	}
			    }
			    finally
			    {
			    	if (state != null)
			    		state.close();
			    }
			    pl.getServer().getLogger().log(Level.INFO,"Création des tables");

				
			} else {
				
				try{
					
					state = conn.createStatement();
					ResultSet verif_column = state.executeQuery("SELECT * FROM joueur");
					ResultSetMetaData verif_MetaData = verif_column.getMetaData();
					
					int nb_column = verif_MetaData.getColumnCount();
					
					if(nb_column != 9)
					{
						
						state.executeUpdate("DROP TABLE joueur");
						InputStream in = pl.getResource("table.sql");
						s = new Scanner(in);
					    s.useDelimiter("/\\*[\\s\\S]*?\\*/|--[^\\r\\n]*|;");
					    
					    try{
					    	 state = conn.createStatement();
					    	
					    	while(s.hasNext())
					    		
					    	{
					    		
					    		String line = s.next().trim();
					    		
					    		if(!line.isEmpty())
					    			state.execute(line);
					    		
					    	}
					    }
					    finally
					    {
					    	if (state != null)
					    		state.close();
					    }
					    pl.getServer().getLogger().log(Level.INFO,ChatColor.GOLD +"Insert data into MySQL database");
					   
						
						
					}
				}
				finally
				{
					if(state != null)
						state.close();
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static void execute(String request) throws SQLException{
		checkConn();
		Statement state = conn.createStatement();
		state.execute(request);
		state.close();
	}
	
	public static  void update(String request) throws SQLException{
		checkConn();
		Statement state = conn.createStatement();
		state.executeUpdate(request);
		state.close();

	}
	
	public static void delete(String request) throws SQLException{
		checkConn();
		Statement state = conn.createStatement();
		state.execute(request);
		state.close();
	}
	
	public static ResultSet select(String request) throws SQLException{
		checkConn();
		ResultSet result;
		Statement state = conn.createStatement();
		result = state.executeQuery(request);
		return result;
	}

	
	private static void checkConn() {
		try{
			Statement state = conn.createStatement();
			state.executeQuery("SELECT 1");
		}catch(SQLException e){
			String url = "jdbc:mysql://"+pl.getConfig().getString("sql.host") + ":" + pl.getConfig().getInt("sql.port") + "/" + pl.getConfig().getString("sql.database")+"?autoReconnect=true";
            try {
				conn = DriverManager.getConnection(url, pl.getConfig().getString("sql.user"), pl.getConfig().getString("sql.passwd"));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
	}

	public static void savehistory(ClientInfo c, TextMessageEvent e) throws SQLException{
		checkConn();
		DateFormat date = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
		Date dat = new Date();
		MySQLUtil.execute("INSERT INTO historique(databaseid, pseudo, datecmd, commande) "
				+ "VALUES('"+c.getDatabaseId()+"', '"+ e.getInvokerName()+"','"+date.format(dat)+"','"+e.getMessage().toLowerCase()+"')");
	}

}
