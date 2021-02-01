package net.mcud.udtitle;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageManager {
	File fPath;
    Map<String, String> msgMap = new HashMap<String, String>();
    List<String> noFound = new ArrayList<String>();
    FileConfiguration config;
    Title plugin;
    public LanguageManager(Title plugin) {
    	this.plugin = plugin;
    }
    
    private String get(String path) {
        return this.msgMap.get(path).replaceAll("&", "§");
    }

    public Map<String, String> getMap() {
        return this.msgMap;
    }

    public boolean loadMsg(FileConfiguration cfg) {
        if (cfg == null) {
            return false;
        }
        this.noFound.clear();
        this.msgMap.clear();
        Lang[] langs = Lang.values();
        Set<String> set = this.config.getConfigurationSection("").getKeys(false);
        for (int i = 0; i < langs.length; i++) {
            if (!set.contains(langs[i].GetPath())) {
                this.noFound.add(langs[i].GetPath());
            } else {
                this.setMsg(langs[i].GetPath(), this.config.getString(langs[i].GetPath().replaceAll("&", "§")));
            }
        }
        if (this.noFound.size() <= 0) {
            return true;
        }
        return false;
    }

    public boolean loadMsg(File path) {
    	this.fPath = path;
        this.config = YamlConfiguration.loadConfiguration(path);
        this.noFound.clear();
        this.msgMap.clear();
        Lang[] langs = Lang.values();
        Set<String> set = this.config.getConfigurationSection("").getKeys(false);
        for (int i = 0; i < langs.length; i++) {
            if (!set.contains(langs[i].GetPath())) {
                this.noFound.add(langs[i].GetPath());
            } else {
                this.setMsg(langs[i].GetPath(), this.config.getString(langs[i].GetPath().replaceAll("&", "§")));
            }
        }
        if (this.noFound.size() > 0) {
            return false;
        }
        return true;
    }

    public List<String> getNoFound() {
        return this.noFound;
    }
    
    public String getMsg(String path) {
        if (this.get(path) == null) {
            return "path";
        }
        return this.get(path);
    }

    public String getMsg(Lang enumlang) {
        return this.get(enumlang.GetPath());
    }

    public void setMsg(Lang enumlang, String msg) {
        this.msgMap.put(enumlang.GetPath(), msg);
    }

    public void setMsg(String path, String msg) {
        this.msgMap.put(path, msg);
    }
}
