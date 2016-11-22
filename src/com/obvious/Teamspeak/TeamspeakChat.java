package com.obvious.Teamspeak;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.obvious.McToTs;
import com.obvious.Utils.MySQLUtil;

public class TeamspeakChat {
	/*
	 * Teamspeak Events
	 */
	static void registerChat() {
		final TS3Api api = TeamspeakBot.getApi();
		api.addTS3Listeners(new TS3EventAdapter(){
			

			@Override
			public void onTextMessage(final TextMessageEvent e){
				if(e.getTargetMode() == TextMessageTargetMode.CHANNEL || e.getTargetMode() == TextMessageTargetMode.CLIENT){

					/*
					 * Kick
					 */
					final String msg = e.getMessage().toLowerCase();
					if(msg.startsWith("!kick"))	api.sendPrivateMessage(e.getInvokerId(), KickCommand.kickCmd(msg, e));
						

					
					/*
					 * Ban/Unban
					 */
					else if (msg.startsWith("!ban")) api.sendPrivateMessage(e.getInvokerId(), BanCommand.banCmd(msg, e));
					else if (msg.startsWith("!unban")) api.sendPrivateMessage(e.getInvokerId(), BanCommand.unbanCmd(msg, e));
	
					
					/*
					 * Mute/TempMute/UnMute
					 */
					else if (msg.startsWith("!mute")) api.sendPrivateMessage(e.getInvokerId(), MuteCommand.muteCmd(msg, e));											
					else if (msg.startsWith("!tempmute")) api.sendPrivateMessage(e.getInvokerId(), MuteCommand.tempmuteCmd(msg, e));														
					else if (msg.startsWith("!unmute")) api.sendPrivateMessage(e.getInvokerId(), MuteCommand.unmuteCmd(msg, e));
					
					/*
					 * Envoi Message
					 */
					else if (msg.startsWith("!msg")) MessageCommand.msgCmd(msg, e);
						
					/*
					 * Broadcast
					 */
					else if (msg.startsWith("!broadcast")) BroadcastCommand.broadcastCmd(msg, e);
					
					
					
					/*
					 * Help
					 */
					else if(msg.equalsIgnoreCase("!help")){
						api.sendPrivateMessage(e.getInvokerId(), "Liste des commandes : ");
						api.sendPrivateMessage(e.getInvokerId(), "!kick <joueur> raison");
						api.sendPrivateMessage(e.getInvokerId(), "!mute <joueur> duree raison");
						api.sendPrivateMessage(e.getInvokerId(), "!ban <joueur> duree raison");
						api.sendPrivateMessage(e.getInvokerId(), "!unmute <pseudo>");
						api.sendPrivateMessage(e.getInvokerId(), "!unban <pseudo");
						api.sendPrivateMessage(e.getInvokerId(), "!broadcast <message>");
						api.sendPrivateMessage(e.getInvokerId(), "!msg <pseudo> <message>");
						api.sendPrivateMessage(e.getInvokerId(), "!history <pseudo/allperm> <nombre de commande>");
					}
					/*
					 * History
					 */
					else if(msg.startsWith("!history")) HistoryCommand.historyCmd(msg, e);																
					/*
					 * Toute commande pour perm allperm
					 */
					 else if(msg.startsWith("!")){
						ClientInfo er = api.getClientInfo(e.getInvokerId());
						ResultSet r = null;
						try {
							r = MySQLUtil.select("SELECT allperm FROM joueur WHERE databaseid = " + er.getDatabaseId());
						} catch (SQLException e1) {
							api.sendPrivateMessage(e.getInvokerId(), "Erreur");
							MySQLUtil.savestacktrace(e1);
							return;
						}
						try {
							while(r.next()){
								boolean allperm = r.getBoolean("allperm");
								if(allperm){
									Bukkit.getScheduler().runTask(McToTs.getPlugin(), new Runnable(){
										public void run(){
											Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), msg.replace("!", "").concat(" @"+e.getInvokerName()));
										}
									});
									api.sendPrivateMessage(e.getInvokerId(), "Commande envoyer");
									MySQLUtil.savehistory(er, e);
								}
								return;
							}
						} catch (CommandException | SQLException e1) {
							MySQLUtil.savestacktrace(e1);
						}
						
					}
					
				
				}
			}
		});
	
		
	}

}
