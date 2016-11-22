package com.obvious.Teamspeak;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.obvious.Utils.MySQLUtil;

public class BroadcastCommand {
	
	private static ResultSet r;

	public static String broadcastCmd(final String msg, final TextMessageEvent e){
		
		final TS3Api api = TeamspeakBot.getApi();
		r = null;
		final ClientInfo er = api.getClientInfo(e.getInvokerId());
		try {
			r = MySQLUtil.select("SELECT broadcast, allperm FROM joueur WHERE databaseid = " + er.getDatabaseId());
		} catch (SQLException e2) {
			api.sendPrivateMessage(e.getInvokerId(), "Erreur MySQL");
			MySQLUtil.savestacktrace(e2);
		}

		
		try {
			while(r.next()){

				boolean broadcast = r.getBoolean("broadcast"), allperm = r.getBoolean("allperm");
				if(broadcast || allperm){
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), msg.replace("!", "").concat(" @"+e.getInvokerName()));
					MySQLUtil.savehistory(er, e);

				}
			}

		} catch (CommandException | SQLException e1) {
			MySQLUtil.savestacktrace(e1);
		}
		return "";
	}

}
