package com.obvious.Teamspeak;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.obvious.Utils.MySQLUtil;

public class MuteCommand {
	/*
	 * Mute
	 */
	static public String muteCmd(final String msg, final TextMessageEvent e)
	{
		TS3Api api = TeamspeakBot.getApi();
		ResultSet r = null;
		ClientInfo er = api.getClientInfo(e.getInvokerId());
		try {
			r = MySQLUtil.select("SELECT mute, allperm FROM joueur WHERE databaseid = '" + er.getDatabaseId()+"'");
		} catch (SQLException e1) {
			MySQLUtil.savestacktrace(e1);
			return "Erreur MySQL";
		}
		try {
			while(r.next()){
				boolean mute = r.getBoolean("mute"), allperm = r.getBoolean("allperm");
				if(mute || allperm){
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), msg.replace("!", "").concat(" @"+e.getInvokerName()));
					MySQLUtil.savehistory(er, e);
					return "Le joueur a était mute";
				}else{
					return "Vous ne pouvez pas mute de joueur";
				}
			}
		} catch (CommandException | SQLException e1) {
			MySQLUtil.savestacktrace(e1);
		}
		return "";
	}
	/*
	 * Tempmute
	 */
	static public String tempmuteCmd(final String msg, final TextMessageEvent e){
		TS3Api api = TeamspeakBot.getApi();
		ResultSet r = null;
		ClientInfo er = api.getClientInfo(e.getInvokerId());
		try {
			r = MySQLUtil.select("SELECT mute, allperm FROM joueur WHERE databaseid = '" + er.getDatabaseId()+"'");
		} catch (SQLException e1) {
			MySQLUtil.savestacktrace(e1);
			return "Erreur MySQL";
		}
		try {
			while(r.next()){
				boolean mute = r.getBoolean("mute"), allperm = r.getBoolean("allperm");
				if(mute || allperm){
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), msg.replace("!", "").concat(" @"+e.getInvokerName()));
					MySQLUtil.savehistory(er, e);
					return "Le joueur a était mute ";

				}else{
					return "Vous ne pouvez pas mute de joueur";
				}
			}
		} catch (CommandException | SQLException e1) {
			MySQLUtil.savestacktrace(e1);
		}
		return "";
	}
	/*
	 * Unmute
	 */
	public static String unmuteCmd(final String msg, final TextMessageEvent e){
		TS3Api api = TeamspeakBot.getApi();

		ClientInfo er = api.getClientInfo(e.getInvokerId());
		ResultSet r = null;
		try {
			r = MySQLUtil.select("SELECT mute, allperm FROM joueur WHERE databaseid = " + er.getDatabaseId());
		} catch (SQLException e1) {
			MySQLUtil.savestacktrace(e1);
			return "Erreur MySQL";
		}
		try {
			while(r.next()){
				boolean mute = r.getBoolean("mute"), allperm = r.getBoolean("allperm");
				if(mute || allperm){
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), msg.replace("!", "").concat(" @"+e.getInvokerName()));
					MySQLUtil.savehistory(er, e);
					return "Le joueur a était unmute ";

				}else{
					return "Vous ne pouvez pas unmute de joueur";
				}
			}
		} catch (CommandException | SQLException e1) {
			MySQLUtil.savestacktrace(e1);
		}
		return "";
	}
}
