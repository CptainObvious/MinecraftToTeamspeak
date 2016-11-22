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

public class BanCommand {
	public static String banCmd(final String msg, TextMessageEvent e){
		TS3Api api = TeamspeakBot.getApi();
		ResultSet r = null;
		ClientInfo er = api.getClientInfo(e.getInvokerId());
		try {
			r = MySQLUtil.select("SELECT ban, allperm FROM joueur WHERE databaseid = '" + er.getDatabaseId()+"'");
		} catch (SQLException e1) {
			MySQLUtil.savestacktrace(e1);
			return "Erreur MySQL";
		}
		try {
			while(r.next()){
				boolean ban = r.getBoolean("ban"), allperm = r.getBoolean("allperm");
				if(ban || allperm){
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), msg.replace("!", "").concat(" @"+e.getInvokerName()));
					MySQLUtil.savehistory(er, e);
					return "Le joueur a était ban";

				}else{
					return "Vous ne pouvez pas ban de joueur";
				}
			}
		} catch (CommandException | SQLException e1) {
			MySQLUtil.savestacktrace(e1);
		}
		return "";


	}
	
	public static String unbanCmd(final String msg, final TextMessageEvent e){
		TS3Api api = TeamspeakBot.getApi();
		ResultSet r = null;
		ClientInfo er = api.getClientInfo(e.getInvokerId());
		try {
			r = MySQLUtil.select("SELECT ban, allperm FROM joueur WHERE databaseid = " + er.getDatabaseId());
		} catch (SQLException e1) {
			MySQLUtil.savestacktrace(e1);
			return "Erreur MySQL";
		}
		try {
			while(r.next()){
				boolean ban = r.getBoolean("ban"), allperm = r.getBoolean("allperm");
				if(ban || allperm){
					Bukkit.getScheduler().runTask(McToTs.getPlugin(), new Runnable(){
						public void run(){
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), msg.replace("!", "").concat(" @"+e.getInvokerName()));
						}
					});
					api.sendPrivateMessage(e.getInvokerId(), "Le joueur a était unban");
					MySQLUtil.savehistory(er, e);

				}else{
					api.sendPrivateMessage(e.getInvokerId(), "Vous ne pouvez pas unban de joueur");
				}
			}
		} catch (CommandException | SQLException e1) {
			MySQLUtil.savestacktrace(e1);
		}
		return "";
	}
}
