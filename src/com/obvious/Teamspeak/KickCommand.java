package com.obvious.Teamspeak;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.obvious.McToTs;
import com.obvious.Utils.MySQLUtil;

public class KickCommand {
	public static String kickCmd(final String msg, final TextMessageEvent e){
		TS3Api api = TeamspeakBot.getApi();
		ResultSet r = null;
		ClientInfo er = api.getClientInfo(e.getInvokerId());
		try {
			r = MySQLUtil.select("SELECT kick, allperm FROM joueur WHERE databaseid = '" + er.getDatabaseId()+"'");
		} catch (SQLException e1) {
			MySQLUtil.savestacktrace(e1);
			return "Erreur MySQL";
		}
		try {
			while(r.next()){
				boolean kick = r.getBoolean("kick"), allperm = r.getBoolean("allperm");
				if(kick || allperm){
					Bukkit.getScheduler().runTask(McToTs.getPlugin(), new Runnable(){
						public void run(){
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), msg.replace("!", "").concat(" @"+e.getInvokerName()));

						}
					});
					MySQLUtil.savehistory(er, e);
					return "Le joueur a était kick";
				}else{
					return "Vous ne pouvez pas kick de joueur";
				}
			}
		} catch (CommandException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			MySQLUtil.savestacktrace(e1);
		}
		return "";

	}
}
