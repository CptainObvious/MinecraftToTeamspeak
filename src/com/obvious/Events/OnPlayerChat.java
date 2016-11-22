package com.obvious.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.massivecraft.factions.entity.MPlayer;
import com.obvious.McToTs;
import com.obvious.Teamspeak.TeamspeakBot;
import com.obvious.Utils.GradeUtil;

import nz.co.lolnet.james137137.FactionChat.API.FactionChatAPI;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class OnPlayerChat implements Listener {
	@EventHandler
	public void onPlayerChat(final AsyncPlayerChatEvent e){
			if(McToTs.isDisable()) return;
			final Player p = e.getPlayer();
			MPlayer mplayer;
			mplayer = MPlayer.get(p);
			final String faction = mplayer.getFactionName();
			final PermissionUser user = PermissionsEx.getUser(p);
			if(!FactionChatAPI.isFactionChatMessage(e)){
				Bukkit.getScheduler().runTaskAsynchronously(McToTs.getPlugin(), new Runnable(){
					
					public void run(){
						String translate = GradeUtil.translatePrefix(user.getPrefix(),p.getName());
						TeamspeakBot.getApi().sendChannelMessage("<[b][color=Darkgreen]"+faction+"[/color][/b] "+translate + "> " + e.getMessage());	
					}
				});
			}
	}
}

	