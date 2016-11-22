package com.obvious.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.obvious.McToTs;
import com.obvious.Teamspeak.TeamspeakBot;
import com.obvious.Utils.MySQLUtil;

public class ReloadCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(!arg1.getName().equalsIgnoreCase("tsreload")) return false;
		if(McToTs.isDisable()) return false;
		if(arg0 instanceof Player){
			Player p = (Player) arg0;
			if(!p.hasPermission("ts.reload")) return false;
			p.sendMessage("§4§l[MC2TS] §4Config Reload");
			Bukkit.getScheduler().runTaskAsynchronously(McToTs.getPlugin(), new Runnable(){
				public void run(){
					TeamspeakBot.disconnect();
					TeamspeakBot.connect(McToTs.getPlugin().getConfig().getString("query.host"), McToTs.getPlugin().getConfig().getString("query.user"), McToTs.getPlugin().getConfig().getString("query.password"), McToTs.getPlugin().getConfig().getInt("query.port"));
					MySQLUtil.connect();
				}
			});
			McToTs.setDisable(McToTs.getPlugin().getConfig().getBoolean("disable"));
		}
		
		McToTs.getPlugin().reloadConfig();
		return false;
	}

}

