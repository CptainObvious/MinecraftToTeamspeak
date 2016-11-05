package com.obvious.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.obvious.McToTs;

public class ReloadCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(!arg1.getName().equalsIgnoreCase("tsreload")) return false;
		if(arg0 instanceof Player){
			Player p = (Player) arg0;
			if(!p.hasPermission("ts.reload")) return false;
			p.sendMessage("§4§l[MC2TS] §4Config Reload");
		}
		
		McToTs.getPlugin().reloadConfig();
		return false;
	}

}

