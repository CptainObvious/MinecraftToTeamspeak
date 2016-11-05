package com.obvious.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.massivecraft.factions.entity.MPlayer;
import com.obvious.TeamspeakBot;

import nz.co.lolnet.james137137.FactionChat.API.FactionChatAPI;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class OnPlayerChat implements Listener {
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
			Player p = e.getPlayer();
			MPlayer mplayer;
			mplayer = MPlayer.get(p);
			String faction = mplayer.getFactionName();
			PermissionUser user = PermissionsEx.getUser(p);
			if(!FactionChatAPI.isFactionChatMessage(e))
				TeamspeakBot.getApi().sendChannelMessage("<[b][color=Darkgreen]"+faction+"[/color][/b] "+"["+user.getPrefix().replaceAll("&\\d", "")+"]"+e.getPlayer().getName() + "> " + e.getMessage());
		
	}
}
