package com.obvious.Teamspeak;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.obvious.Utils.MySQLUtil;

public class MessageCommand {
	
	public static String msgCmd(final String msg, TextMessageEvent e)
	{
		String[] msgsplit = msg.split("\\s+");
		TS3Api api = TeamspeakBot.getApi();
		ResultSet r = null;
		ClientInfo er = api.getClientInfo(e.getInvokerId());
		try {
			r = MySQLUtil.select("SELECT msg, allperm FROM joueur WHERE databaseid = " + er.getDatabaseId());
		} catch (SQLException e1) {
			MySQLUtil.savestacktrace(e1);
			return "Erreur MySQL";
		}
		try {
			while(r.next()){
				boolean msgb = r.getBoolean("msg"), allperm = r.getBoolean("allperm");
				if(msgb || allperm){
					@SuppressWarnings("deprecation")
					Player receiver = Bukkit.getServer().getPlayer(msgsplit[1]);
					if(receiver == null) return "";
					receiver.sendMessage(ChatColor.RED + "[EpicBot] " + ChatColor.DARK_GREEN + "Un membre du staff vous a envoyé un message depuis Teamspeak");
					receiver.sendMessage(ChatColor.ITALIC +"[TS]"+ msg.replace("!msg "+msgsplit[1], "").concat(" @" + e.getInvokerName()));
					MySQLUtil.savehistory(er, e);

				}
			}
		} catch (CommandException | SQLException e1) {
			MySQLUtil.savestacktrace(e1);
		}
		return "";
	}

}
