package com.obvious.Commands;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.obvious.McToTs;
import com.obvious.Utils.MySQLUtil;

public class AddClientCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(!arg2.equalsIgnoreCase("tsadd")) return false;
		if(McToTs.isDisable()) 				return false;
		if(arg3.length != 2)			 	return false;
		if(!(arg0 instanceof Player))		return false;		
		Player p = (Player) arg0;
		if(!p.hasPermission("ts.add")) 		return false;
		try {
			switch(arg3[1]){
			
				case "mdc":
					if(!alreadyexist(Integer.parseInt(arg3[0]))){
						
						MySQLUtil.execute("INSERT INTO joueur(databaseid) VALUES("+Integer.parseInt(arg3[0])+")");
						arg0.sendMessage(ChatColor.DARK_GREEN + "Identifiant : " +Integer.parseInt(arg3[0])+ " ajouté");
						
					}
					else
					{
						
						arg0.sendMessage(ChatColor.RED + "L'identifiant est déjà dans la base, supprimer le d'abord");
						
					}
					break;
				case "modo":
					if(!alreadyexist(Integer.parseInt(arg3[0]))){
						MySQLUtil.execute("INSERT INTO joueur(databaseid, ban, kick,broadcast) VALUES("+Integer.parseInt(arg3[0])+",true,true,true)");
						arg0.sendMessage(ChatColor.DARK_GREEN + "Identifiant : " +Integer.parseInt(arg3[0])+ " ajouté");

					}
					else
					{
						
						arg0.sendMessage(ChatColor.RED + "L'identifiant est déjà dans la base, supprimer le d'abord");
						
					}
					break;
				case "admin":
					if(!alreadyexist(Integer.parseInt(arg3[0]))){
						
						MySQLUtil.execute("INSERT INTO joueur(databaseid, allperm) VALUES("+Integer.parseInt(arg3[0])+", 1)");
						arg0.sendMessage(ChatColor.DARK_GREEN + "Identifiant " +Integer.parseInt(arg3[0])+ " ajouté");
						
					}
					else
					{
						
						arg0.sendMessage(ChatColor.RED + "L'identifiant est déjà dans la base, supprimer le d'abord");
						
					}
					break;
				}
		} catch (SQLException e) {
			arg0.sendMessage(ChatColor.RED + "Identifiant " +Integer.parseInt(arg3[0])+ " fail");
			MySQLUtil.savestacktrace(e);
		}

		return false;
	}

	private boolean alreadyexist(int databaseid) throws SQLException {
		ResultSet r = MySQLUtil.select("SELECT databaseid FROM joueur WHERE databaseid = "+databaseid);
		if(r.next()){
			r.close();
			return true;
		}
		r.close();	
		return false;
	}

}
