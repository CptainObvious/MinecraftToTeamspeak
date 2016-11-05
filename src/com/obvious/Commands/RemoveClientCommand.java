package com.obvious.Commands;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.obvious.Utils.MySQLUtil;

public class RemoveClientCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(!arg2.equalsIgnoreCase("tsremove")) return false;
		if(arg3.length != 1) return false;
		if(arg0 instanceof Player){
			Player p = (Player) arg0;
			if(!p.hasPermission("ts.remove")) return false;
		}
		
		try {
			MySQLUtil.execute("DELETE FROM joueur WHERE databaseid = "+Integer.parseInt(arg3[0])+"");
			arg0.sendMessage(ChatColor.DARK_GREEN + "Identifiant : " +Integer.parseInt(arg3[0])+ " remove");
		} catch (SQLException e) {
			arg0.sendMessage(ChatColor.RED + "Identifiant : " +Integer.parseInt(arg3[0])+ " fail");
			MySQLUtil.savestacktrace(e);
		}

		return false;
	}
}
