package net.mcud.udtitle;

import net.mcud.udtitle.listener.PlayerEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.mcud.udtitle.command.TGui;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("deprecation")
public class Title extends JavaPlugin {
    // @Deprecation
	// public List<Integer> LTI;
    // @Deprecation
    // public List<String> LTS;
    
    public Map<Integer, String> titleMap;
    
    public int storageType;
    public FileConfiguration pluginConfig;
    public String cmd;
    public double cost;
    public Economy econApi = null;
    public File configFile;
    public String listGuiTitle;
    public String titleGuiTitle;
    public Permission permsApi = null;
    public boolean isUseCommand;
    public boolean isUseGui;
    public int defaultTitleId = 1001;
    // 未使用
    // public LanguageManager DLM;
    public LanguageManager msgManager;
    public PlayerData data;
	public net.mcud.udtitle.Placeholder papiExpansion;
    public TGui commands;
    public PlayerEvent playerEvents;
    public GuiTitle guiTitle;
    public ListGui guiList;
    
    public void loadDefaultMsg() {
    	InputStream is = getResource("Msg.yml");
    	if(is != null) {
        	pluginConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(is));
        	this.msgManager.config = pluginConfig;
    	}
    	else {
    		this.getLogger().info("找不到插件内置语言文件，请检查插件是否完整");
    	}
    }

    public void reload() {
        this.saveResource("Msg.yml", false);
        if (this.msgManager == null) {
            this.msgManager = new LanguageManager(this);
        }
        if (!this.msgManager.loadMsg(new File(this.getDataFolder(), "Msg.yml"))) {
            loadDefaultMsg();
        }
        this.configFile = new File(this.getDataFolder(), "config.yml");
        if (configFile.exists()) {
            pluginConfig = YamlConfiguration.loadConfiguration(configFile);
        } else {
            saveDefaultConfig();
            configFile = new File(this.getDataFolder(), "config.yml");
            pluginConfig = YamlConfiguration.loadConfiguration(configFile);
        }
        if (pluginConfig == null) {
            info(this.msgManager.getMsg(Lang.LOADFAIL));
        }
        GuiTitle.prevPage = packId(-233) + this.msgManager.getMsg(Lang.LASTPAGE);
        GuiTitle.nextPage = packId(-233) + this.msgManager.getMsg(Lang.NEXTPAGE);
        GuiTitle.cancelTag = packId(-233) + this.msgManager.getMsg(Lang.CANCELTITLE);
        ListGui.prevPage = packId(-233) + this.msgManager.getMsg(Lang.LASTPAGE);
        ListGui.nextPage = packId(-233) + this.msgManager.getMsg(Lang.NEXTPAGE);
        titleGuiTitle = pluginConfig.getString("title");
        if ((titleGuiTitle == null) || (titleGuiTitle.length() < 1)) {
            titleGuiTitle = "&b称号列表";
            pluginConfig.set("title", titleGuiTitle);
        }
        titleGuiTitle = titleGuiTitle.replace("&", "§");
        listGuiTitle = pluginConfig.getString("listtitle");
        this.defaultTitleId = pluginConfig.getInt("defaultTitleId");
        if (listGuiTitle == null || listGuiTitle.length() < 1) {
            listGuiTitle = "&a所有称号展示";
            pluginConfig.set("listtitle", listGuiTitle);
        }
        listGuiTitle = listGuiTitle.replace("&", "§");
        isUseCommand = pluginConfig.getBoolean("usecommand");
        cmd = pluginConfig.getString("cmd");
        storageType = pluginConfig.getInt("StorageType");
        cost = pluginConfig.getDouble("cost");
        isUseGui = pluginConfig.getBoolean("usegui");
        this.loadTitle();
        this.data.loadConfiguration();
    }

    public GuiTitle getGuiTitle() {
    	return guiTitle;
    }
    public ListGui getGuiList() {
    	return guiList;
    }
    
    public void onEnable() {
    	commands = new TGui(this);
    	playerEvents = new PlayerEvent(this);
    	guiTitle = new GuiTitle(this);
    	guiList = new ListGui(this);
        this.getCommand("tgui").setExecutor(commands);
        this.getServer().getPluginManager().registerEvents(playerEvents, this);
	    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
	    	this.installPlaceholderHook();
	    }
        this.data = new PlayerData(this);
        reload();
        if (!setupPermissions() || !setupEconomy()) {
            info("获取经济,权限管理失败,请检查是否安装了Vault,经济,权限插件");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        }
        else info("UDtitle 已开启");
    }

    public void installPlaceholderHook() {
    	this.papiExpansion = new Placeholder(this);
        
        if (this.papiExpansion.register()) {
            this.getLogger().info("PlaceholderAPI successfully hooked");
        }
        else {
            this.getLogger().info("PlaceholderAPI unsuccessfully hooked");
        }
    }
    
    public void onDisable() {
        info("UDtitle 已关闭");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econApi = (Economy) rsp.getProvider();
        return econApi != null;
    }

    private boolean setupPermissions() {
        permsApi = (Permission) getServer().getServicesManager().getRegistration(Permission.class).getProvider();
        return permsApi != null;
    }

    public void info(String str) {
        getLogger().info(str);
    }

    public List<String> getPlayerAllTitle(String player) {
        List<String> listTitle = new ArrayList<String>();
    	for (Integer titleId : titleMap.keySet()) {
            if (hasTitle(player, titleId)) {
                listTitle.add(titleMap.get(titleId));
            }
        }
        return listTitle;
    }

    public Boolean isTitleExist(int id) {
        return Boolean.valueOf(this.titleMap.keySet().contains(id));
    }

    public List<Integer> getPlayerAllTitleID(String player) {
        List<Integer> listId = new ArrayList<Integer>();
        for (Integer titleId : titleMap.keySet()) {
            if (hasTitle(player, titleId)) {
                listId.add(titleId);
            }
        }
        return listId;
    }

    public List<Integer> getAllTitleID() {
        List<Integer> L = new ArrayList<>();
        Set<String> S = pluginConfig.getConfigurationSection("titles").getKeys(false);
        if (!S.isEmpty()) {
            for (String str : S) {
                L.add(Integer.valueOf(str));
            }
        }
        return L;
    }

    public String getTitleForTitltID(int id) {
        if(titleMap.keySet().contains(id)) {
        	return titleMap.get(id);
        }
        return "";
    }

    public List<String> getLore(int id) {
        List<String> L = pluginConfig.getStringList("lore." + id);
        for (int i = 0; i < L.size(); i++) {
            L.set(i, L.get(i).replace("&", "§"));
        }
        return L;
    }

    public String getTitleItemID(int id) {
        return pluginConfig.getString("itemid." + id + ".Material");
    }

    public int getTitleItemDataID(int id) {
        return pluginConfig.getInt("itemid." + id + ".Data");
    }

    public List<String> getAllTitle() {
        List<Integer> L = this.getAllTitleID();
        List<String> S = new ArrayList<>();
        for (Integer I : L) {
            S.add(String.valueOf(packId(I.intValue())) + "§r" + pluginConfig.getString("titles." + String.valueOf(I)).replace("&", "§"));
        }
        return S;
    }

    public void loadTitle() {
    	titleMap = new HashMap<Integer, String>();
        ConfigurationSection cs = pluginConfig.getConfigurationSection("titles");
        for(String id : cs.getKeys(false)) {
        	try {
        		Integer intId = Integer.valueOf(id);
        		String tag = cs.getString(id);
        		titleMap.put(intId, tag);
        	}catch(Throwable t){
        		this.info("载入称号(id=" + id + ")时出现异常: " + t.getLocalizedMessage());
        		continue;
        	}
        }
    }

    public boolean addPlayerTitle(String player, int id) {
        if (storageType != 0) {
            return permsApi.playerAdd((String) null, player, "udtitle.t." + String.valueOf(id));
        }
        List<Integer> L = pluginConfig.getIntegerList("players." + player.toLowerCase());
        if (!L.contains(Integer.valueOf(id)) && isTitleExist(id).booleanValue()) {
            L.add(Integer.valueOf(id));
            pluginConfig.set("players." + player.toLowerCase(), L);
        }
        try {
            pluginConfig.save(configFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removePlayerTitle(String player, int id) {
        if (storageType != 0) {
            return permsApi.playerRemove((String) null, player, "udtitle.t." + String.valueOf(id));
        }
        List<Integer> L = pluginConfig.getIntegerList("players." + player);
        for (int i = 0; i < L.size(); i++) {
            if (L.get(i).intValue() == id) {
                L.remove(i);
                return true;
            }
        }
        pluginConfig.set("players." + player, L);
        try {
            pluginConfig.save(configFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> DefaultLore() {
        List<String> L = new ArrayList<>();
        L.add("§bO(∩_∩)O");
        L.add("§aQwQ");
        return L;
    }

    public String packId(int i) {
        StringBuilder result = new StringBuilder();
        for (char c2 : String.valueOf(i).toCharArray()) {
            result.append("§").append(c2);
        }
        return result.toString();
    }

    public int extractId(String s) {
        int resetIndex = s.indexOf("§r");
        if (resetIndex <= 0) {
            return -1;
        }
        String codes = s.substring(0, resetIndex);
        int id = 0;
        for (int i = 1; i < codes.length(); i += 2) {
            id = (id * 10) + (codes.charAt(i) - '0');
        }
        return id;
    }

    public boolean hasTitle(String player, int id) {
        if (storageType != 0) {
            return permsApi.has("", player, "udtitle.t." + String.valueOf(id));
        }
        if (!permsApi.has("", player, "udtitle.t.*") && !pluginConfig.getIntegerList("players." + player.toLowerCase()).contains(Integer.valueOf(id))) {
            return false;
        }
        return true;
    }

    public void setPlayerTitle(Player player, String prefix) {
        setPlayerTitle(player.getName(), prefix);
    }

    public boolean setPlayerTitle(String player, String prefix) {
        data.setPlayerTag(player, prefix);
        data.saveConfiguration();
        if (isUseCommand) {
            return Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("%player%", player).replace("%prefix%", prefix).replace("§", "&"));
        }
        return true;
    }

    public boolean setPlayerTitle(Player player, int id) {
        return setPlayerTitle(player.getName(), id);
    }

	public boolean setPlayerTitleUseMoney(Player player, int id) {
        if (econApi.getBalance(player.getName()) < cost || !takeMoney(player.getName(), cost)) {
            return false;
        }
        if (setPlayerTitle(player, id)) {
            return true;
        }
        econApi.depositPlayer(player.getName(), cost);
        player.sendMessage(msgManager.getMsg(Lang.NOTENOUGHMONEY));
        return false;
    }

    public boolean takeMoney(String player, double db) {
        return econApi.withdrawPlayer(player, db).transactionSuccess();
    }

    public boolean setPlayerTitle(String player, int id) {
        for (Integer titleId : titleMap.keySet()) {
            if (titleId == id) {
                return setPlayerTitle(player, titleMap.get(titleId));
            }
        }
        return false;
    }

    public void setDefaultPrefix(Player player) {
        this.setPlayerTitle(player, "&7[&a玩家&7]&e");
    }
    

    public static Material valueOf(String str, Material nullValue) {
    	for(Material m : Material.values()) {
    		if(m.name().equalsIgnoreCase(str)) {
    			return m;
    		}
    	}
    	return nullValue;
    }
}
