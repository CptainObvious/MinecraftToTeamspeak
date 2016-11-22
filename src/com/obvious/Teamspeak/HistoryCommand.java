package com.obvious.Teamspeak;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.command.CommandException;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.obvious.Utils.MySQLUtil;

public class HistoryCommand {
	
	private static ResultSet r1;

	public static String historyCmd(String msg, TextMessageEvent e){
		
		TS3Api api = TeamspeakBot.getApi();
		String[] msgsplit = msg.split("\\s+");
		ClientInfo er = api.getClientInfo(e.getInvokerId());
		ResultSet r = null;
		String text = "\nDate 				Utilisateur 				Commandes\n";
		try {
			r = MySQLUtil.select("SELECT history, allperm FROM joueur WHERE databaseid = " + er.getDatabaseId());
			try {
				while(r.next()){
					boolean broadcast = r.getBoolean("history"), allperm = r.getBoolean("allperm");
					if(broadcast || allperm){
						if(msgsplit.length == 3){
							
							if(msgsplit[1].equalsIgnoreCase("all")){
								int nb = Integer.valueOf(msgsplit[2]);
								r1 = MySQLUtil.select("SELECT * FROM historique ORDER BY id DESC LIMIT "+nb);
							}else{
								int nb = Integer.valueOf(msgsplit[2]);
								r1 = MySQLUtil.select("SELECT * FROM historique WHERE databaseid = "+Integer.valueOf(msgsplit[1])+" ORDER BY id DESC LIMIT "+nb);
							}
							while(r1.next()){
								text+=  "["+r1.getString("datecmd")+"]"+r1.getString("pseudo")+" :"+r1.getString("commande")+"\n";
							}
							api.sendPrivateMessage(e.getInvokerId(), text);


						}
						MySQLUtil.savehistory(er, e);
					}
				}
			} catch (CommandException | SQLException e1) {
				MySQLUtil.savestacktrace(e1);
			}
		} catch (SQLException e1) {
			MySQLUtil.savestacktrace(e1);
			return "Erreur MySQL";
		}
		return text;
	}

}
