package com.obvious.Utils;

public class GradeUtil {
	
	
	public static String translatePrefix(String prefix, String nom) {
		switch (prefix){
		case "&6[Paladin] &f":
			return "[color=#ADD8E6][Paladin][/color] "+nom;		
		case "&6[Guerrier] &f":
			return "[color=#ADD8E6][Guerrier][/color] "+nom;
		case "&b[VIP] &f":
			return "[color=aqua][VIP][/color] "+nom;
		case "&b[VIP+] &3":
			return "[color=aqua][VIP+][/color][color=#008080] "+nom+"[/color]";
		case "&3[&b&lLegendaire&3] &3":
			return "[color=#008080][[/color][color=aqua][B]Legendaire[/B][/color][color=#008080]] "+nom+" [/color]";
		case "&6&l&n[Ultime]&r &3&l":
			return "[B][U][color=#ADD8E6][Ultime][/color][/U][color=#008080] "+nom+" [/B][/color]";
		case "&c&l[MDC] &b&l":
			return "[color=red][B][MDC] [/B][/color][color=aqua][B] "+nom+"[/B][/color]";
		case "&1[Modo] &c&l":
			return "[color=darkblue][Modo][/color][color=red][B] "+nom+"[/B]";
		case "&1[Moderateur]&d":
			return "[color=darkblue][Moderateur][/color][color=#FF55FF] "+nom+"[color=red]";
		case "&4[Fondateur]":
			return "[color=darkred][Fondateur] "+nom;
		}
		return nom;
	}
}
