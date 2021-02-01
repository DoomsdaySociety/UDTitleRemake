package net.mcud.udtitle;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerData {
	FileConfiguration config;
	File file;
	Title plugin;
	public PlayerData(Title plugin) {
		this.plugin = plugin;
		this.loadConfiguration();
	}
	
	public void loadConfiguration() {
		file = new File(plugin.getDataFolder().getAbsolutePath() + "\\data.yml");
		try {
		if(!file.exists()) file.createNewFile();
			config = YamlConfiguration.loadConfiguration(file);
		}catch(Throwable t) {
			t.printStackTrace();
			config = new YamlConfiguration();
		}
	}
	
	public void saveConfiguration() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setPlayerTag(String player, String tag) {
		config.set(player, tag);
	}
	
	public String getPlayerTag(String player) {
		if(!config.contains(player)) {
			if(plugin.titleMap.keySet().contains(plugin.defaultTitleId)) {
				config.set(player, plugin.titleMap.get(plugin.defaultTitleId));
			}
			else {
				config.set(player, "&7[&a玩家&7]&e");
			}
		}
		return config.getString(player);
	}
}
