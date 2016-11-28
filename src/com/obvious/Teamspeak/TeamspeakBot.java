package com.obvious.Teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.obvious.McToTs;

public class TeamspeakBot {

	/*
	 * Connect Teamspeak
	 */
	
	private static TS3Query query;
	private static TS3Api api;

	public static void connect(String host, String user, String pwd, int port){
		final TS3Config config = new TS3Config();
		config.setHost(host);
		config.setDebugToFile(false);
		config.setQueryPort(port);
		
		query = new TS3Query(config);		
		api = query.getApi();
		api.login(user, pwd);
		api.selectVirtualServerById(1);
		api.setNickname(McToTs.getPlugin().getConfig().getString("bot.name"));
		api.moveQuery(McToTs.getPlugin().getConfig().getInt("teamspeak.chanId"));
		api.registerAllEvents();
		TeamspeakChat.registerChat();
		
	}
	

	@SuppressWarnings("deprecation")
	public static void disconnect() {
		if(api != null){
			api.quit();
			api.logout();
			api = null;
		}
		if(query != null){
		query.exit();
		
		query = null;
		}
		
	}
	
	public static TS3Api getApi(){
		return api;
	}

}
