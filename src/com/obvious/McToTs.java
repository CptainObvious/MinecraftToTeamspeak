package com.obvious;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.obvious.Commands.AddClientCommand;
import com.obvious.Commands.ReloadCommand;
import com.obvious.Commands.RemoveClientCommand;
import com.obvious.Events.OnPlayerChat;
import com.obvious.Utils.JarUtils;
import com.obvious.Utils.MySQLUtil;

public class McToTs extends JavaPlugin implements Listener{
	private static Plugin plugin;


	public void onEnable(){
		loadLib();
		plugin = this;
		/*
		 * Création du fichier config
		 */
		if(!new File(this.getDataFolder(), "config.yml").exists()){
			saveDefaultConfig();
			Bukkit.getLogger().info("Plugin disable, remplir le fichier config");;
			Bukkit.getPluginManager().disablePlugin(this);
			return ;
			
		}
		loadLib();

		
		/*
		 * Register Commands
		 */
	    getCommand("tsreload").setExecutor(new ReloadCommand());
	    getCommand("tsadd").setExecutor(new AddClientCommand());
	    getCommand("tsremove").setExecutor(new RemoveClientCommand());
	    
	    /*
	     * Connect to the database
	     */
	    MySQLUtil.connect();
		Bukkit.getServer().getPluginManager().registerEvents(new OnPlayerChat(), this);
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
			@Override
			public void run(){
				TeamspeakBot.connect(getConfig().getString("query.host"), getConfig().getString("query.user"), getConfig().getString("query.password"), getConfig().getInt("query.port"));
			}
			
		});

		
	}
	

	public void onDisable(){
		TeamspeakBot.disconnect();
		plugin = null;
	}
	
	
	/*
	 * Load Lib
	 */
	public void loadLib(){
		  try {
	            final File lib = new File(getDataFolder(), "/lib/teamspeak3-api-1.0.13.jar");
	            if (!lib.exists()) {
	                JarUtils.extractFromJar(lib.getName(),
	                lib.getAbsolutePath());
	            }
	           
	            if (!lib.exists()) {
	               getLogger().warning(
	                            "Impossible de charger la librairie: "
	                                    + lib.getName());
	                Bukkit.getServer().getPluginManager().disablePlugin(this);
	                return;
	                }
	                addClassPath(JarUtils.getJarUrl(lib));
	            
	        } catch (final Exception e) {
	            e.printStackTrace();
	        }
	}
	
	private void addClassPath(final URL url) throws IOException {
        final URLClassLoader sysloader = (URLClassLoader) ClassLoader
                .getSystemClassLoader();
        final Class<URLClassLoader> sysclass = URLClassLoader.class;
        try {
            final Method method = sysclass.getDeclaredMethod("addURL",
                    new Class[] { URL.class });
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { url });
        } catch (final Throwable t) {
            t.printStackTrace();
            throw new IOException("Error adding " + url
                    + " to system classloader");
        }
    }

	
	public static Plugin getPlugin(){
		return plugin;
	}

}
