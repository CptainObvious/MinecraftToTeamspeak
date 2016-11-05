package com.obvious;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.obvious.Utils.MySQLUtil;

public class TeamspeakBot {

	/*
	 * Connect Teamspeak
	 */
	
	private static TS3Query query;
	private static TS3Api api;

	public static void connect(String host, String user, String pwd, int port){
		final TS3Config config = new TS3Config();
		config.setHost(host);
		config.setDebugToFile(true);
		config.setQueryPort(port);
		
		query = new TS3Query(config);
		
		query.connect();
		
		api = query.getApi();
		api.login(user, pwd);
		api.selectVirtualServerById(1);
		api.setNickname(McToTs.getPlugin().getConfig().getString("bot.name"));
		api.moveQuery(McToTs.getPlugin().getConfig().getInt("teamspeak.chanId"));
		
		api.registerAllEvents();
		registerChat();
		
	}
	
	/*
	 * Teamspeak Events
	 */
	private static void registerChat() {
		api.addTS3Listeners(new TS3EventAdapter(){
			
			private ResultSet r1;

			@SuppressWarnings({ "deprecation", "unused" })
			@Override
			public void onTextMessage(final TextMessageEvent e){
				if(e.getTargetMode() == TextMessageTargetMode.CHANNEL || e.getTargetMode() == TextMessageTargetMode.CLIENT){

					/*
					 * Kick
					 */
					final String msg = e.getMessage().toLowerCase();
					if(msg.startsWith("!kick")){
						String[] msgsplit = msg.split("\\s+");
						ResultSet r = null;
						ClientInfo er = api.getClientInfo(e.getInvokerId());
						try {
							r = MySQLUtil.select("SELECT kick, allperm FROM joueur WHERE databaseid = '" + er.getDatabaseId()+"'");
						} catch (SQLException e1) {
							api.sendPrivateMessage(e.getInvokerId(), "Erreur");
							MySQLUtil.savestacktrace(e1);
							return;
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
									api.sendPrivateMessage(e.getInvokerId(), "Le joueur a était kick");
									MySQLUtil.savehistory(er, e);
								}else{
									api.sendPrivateMessage(e.getInvokerId(), "Vous ne pouvez pas kick de joueur");
								}
								return;
							}
						} catch (CommandException e1) {
							e1.printStackTrace();
						} catch (SQLException e1) {
							MySQLUtil.savestacktrace(e1);
						}

					}
					/*
					 * Ban
					 */
					else if (msg.startsWith("!ban")){
						String[] msgsplit = msg.split("\\s+");
						ResultSet r = null;
						ClientInfo er = api.getClientInfo(e.getInvokerId());
						try {
							r = MySQLUtil.select("SELECT ban, allperm FROM joueur WHERE databaseid = '" + er.getDatabaseId()+"'");
						} catch (SQLException e1) {
							api.sendPrivateMessage(e.getInvokerId(), "Erreur");
							MySQLUtil.savestacktrace(e1);
							return;
						}
						try {
							while(r.next()){
								boolean ban = r.getBoolean("ban"), allperm = r.getBoolean("allperm");
								if(ban || allperm){
									Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), msg.replace("!", "").concat(" @"+e.getInvokerName()));
									api.sendPrivateMessage(e.getInvokerId(), "Le joueur a était ban");
									MySQLUtil.savehistory(er, e);

								}else{
									api.sendPrivateMessage(e.getInvokerId(), "Vous ne pouvez pas ban de joueur");
								}
								return;
							}
						} catch (CommandException | SQLException e1) {
							MySQLUtil.savestacktrace(e1);
						}


					}
					/*
					 * Mute
					 */
					else if (msg.startsWith("!mute")){
						String[] msgsplit = msg.split("\\s+");
						ResultSet r = null;
						ClientInfo er = api.getClientInfo(e.getInvokerId());
						try {
							r = MySQLUtil.select("SELECT mute, allperm FROM joueur WHERE databaseid = '" + er.getDatabaseId()+"'");
						} catch (SQLException e1) {
							api.sendPrivateMessage(e.getInvokerId(), "Erreur");
							MySQLUtil.savestacktrace(e1);
							return;
						}
						try {
							while(r.next()){
								boolean mute = r.getBoolean("mute"), allperm = r.getBoolean("allperm");
								if(mute || allperm){
									Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), msg.replace("!", "").concat(" @"+e.getInvokerName()));
									api.sendPrivateMessage(e.getInvokerId(), "Le joueur a était mute ");
									MySQLUtil.savehistory(er, e);
								}else{
									api.sendPrivateMessage(e.getInvokerId(), "Vous ne pouvez pas mute de joueur");
								}
								return;
							}
						} catch (CommandException | SQLException e1) {
							MySQLUtil.savestacktrace(e1);
						}
					}
					/*
					 * UnBan
					 */
					else if (msg.startsWith("!unban")){
						String[] msgsplit = msg.split("\\s+");
						ResultSet r = null;
						ClientInfo er = api.getClientInfo(e.getInvokerId());
						try {
							r = MySQLUtil.select("SELECT ban, allperm FROM joueur WHERE databaseid = " + er.getDatabaseId());
						} catch (SQLException e1) {
							api.sendPrivateMessage(e.getInvokerId(), "Erreur");
							MySQLUtil.savestacktrace(e1);
							return;
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
								return;
							}
						} catch (CommandException | SQLException e1) {
							MySQLUtil.savestacktrace(e1);
						}
					}
					/*
					 * UnMute
					 */
					else if (msg.startsWith("!unmute")){
						String[] msgsplit = msg.split("\\s+");
						ClientInfo er = api.getClientInfo(e.getInvokerId());
						ResultSet r = null;
						try {
							r = MySQLUtil.select("SELECT mute, allperm FROM joueur WHERE databaseid = " + er.getDatabaseId());
						} catch (SQLException e1) {
							api.sendPrivateMessage(e.getInvokerId(), "Erreur");
							MySQLUtil.savestacktrace(e1);
							return;
						}
						try {
							while(r.next()){
								boolean mute = r.getBoolean("mute"), allperm = r.getBoolean("allperm");
								if(mute || allperm){
									Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), msg.replace("!", "").concat(" @"+e.getInvokerName()));
									api.sendPrivateMessage(e.getInvokerId(), "Le joueur a était unmute ");
									MySQLUtil.savehistory(er, e);

								}else{
									api.sendPrivateMessage(e.getInvokerId(), "Vous ne pouvez pas unmute de joueur");
								}
								return;
							}
						} catch (CommandException | SQLException e1) {
							MySQLUtil.savestacktrace(e1);
						}
					}
					/*
					 * Envoi Message
					 */
					else if (msg.startsWith("!msg")){
						String[] msgsplit = msg.split("\\s+");
						ResultSet r = null;
						ClientInfo er = api.getClientInfo(e.getInvokerId());
						try {
							r = MySQLUtil.select("SELECT msg, allperm FROM joueur WHERE databaseid = " + er.getDatabaseId());
						} catch (SQLException e1) {
							api.sendPrivateMessage(e.getInvokerId(), "Erreur");
							MySQLUtil.savestacktrace(e1);
							return;
						}
						try {
							while(r.next()){
								boolean msgb = r.getBoolean("msg"), allperm = r.getBoolean("allperm");
								if(msgb || allperm){
									Player receiver = Bukkit.getServer().getPlayer(msgsplit[1]);
									if(receiver == null) return;
									receiver.sendMessage(ChatColor.RED + "[EpicBot] " + ChatColor.DARK_GREEN + "Un membre du staff vous a envoyé un message depuis Teamspeak");
									receiver.sendMessage(ChatColor.ITALIC +"[TS]"+ msg.replace("!msg "+msgsplit[1], "").concat(" @" + e.getInvokerName()));
									MySQLUtil.savehistory(er, e);

								}
								return;
							}
						} catch (CommandException | SQLException e1) {
							MySQLUtil.savestacktrace(e1);
						}
					}
					/*
					 * Broadcast
					 */
					else if (msg.startsWith("!broadcast")){
						String[] msgsplit = msg.split("\\s+");
						ResultSet r = null;
						ClientInfo er = api.getClientInfo(e.getInvokerId());
						try {
							r = MySQLUtil.select("SELECT broadcast, allperm FROM joueur WHERE databaseid = " + er.getDatabaseId());
							try {
								while(r.next()){
									boolean broadcast = r.getBoolean("broadcast"), allperm = r.getBoolean("allperm");
									if(broadcast || allperm){
										Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), msg.replace("!", "").concat(" @"+e.getInvokerName()));
										MySQLUtil.savehistory(er, e);

									}
									return;
								}

							} catch (CommandException | SQLException e1) {
								MySQLUtil.savestacktrace(e1);
							}
						} catch (SQLException e1) {
							MySQLUtil.savestacktrace(e1);
							return;
						}
					}
					
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
					else if(msg.startsWith("!history")){
						String[] msgsplit = msg.split("\\s+");
						ClientInfo er = api.getClientInfo(e.getInvokerId());
						ResultSet r = null;
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
												api.sendPrivateMessage(e.getInvokerId(), "["+r1.getString("datecmd")+"]"+r1.getString("pseudo")+" :"+r1.getString("commande"));
											}

										}
										MySQLUtil.savehistory(er, e);
									return;
									}
								}
							} catch (CommandException | SQLException e1) {
								MySQLUtil.savestacktrace(e1);
							}
						} catch (SQLException e1) {
							api.sendPrivateMessage(e.getInvokerId(), "Erreur");
							MySQLUtil.savestacktrace(e1);
							return;
						}
						
					}
					/*
					 * Toute commande pour perm allperm
					 */
					 else if(msg.startsWith("!")){
						String[] msgsplit = msg.split("\\s+");
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

	public static void disconnect() {
		if(query != null){
		query.exit();
		query = null;
		}
		if(api != null){
		api.logout();
		api = null;
		}
		
	}
	
	public static TS3Api getApi(){
		return api;
	}

}
