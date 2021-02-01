package net.mcud.udtitle.command;

import java.util.List;
import net.mcud.udtitle.Lang;
import net.mcud.udtitle.Title;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class TGui implements CommandExecutor {
	Title plugin;
	public TGui(Title plugin) {
		this.plugin = plugin;
	}
	
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] s) {
        Player p = null;
        
        if (arg0 instanceof Player) {
            p = (Player) arg0;
        }
        if (s.length == 0) {
            if (!plugin.isUseGui) {
                arg0.sendMessage(plugin.msgManager.getMsg(Lang.CanNotOpenGui));
                return true;
            }
            plugin.getGuiTitle().open(p, 1);
        } else if (s.length == 1) {
        	if(s[0].equalsIgnoreCase("help")) {
                arg0.sendMessage(plugin.msgManager.getMsg(Lang.help1));
                arg0.sendMessage(plugin.msgManager.getMsg(Lang.help2));
                arg0.sendMessage(plugin.msgManager.getMsg(Lang.help3));
        	}
        	else if (s[0].equalsIgnoreCase("open")) {
                if (!plugin.isUseGui) {
                    arg0.sendMessage(plugin.msgManager.getMsg(Lang.CanNotOpenGui));
                    return true;
                }
                plugin.getGuiTitle().open(p, 1);
            } else if (s[0].equalsIgnoreCase("list")) {
                if (arg0 instanceof Player) {
                	plugin.getGuiList().open(p, 1);
                    return true;
                }
                arg0.sendMessage(plugin.msgManager.getMsg(Lang.List1));
                
                for(Integer id : plugin.titleMap.keySet()) {
                    arg0.sendMessage("§4" + id + "§b:---:" + plugin.titleMap.get(id));
                }
                arg0.sendMessage(plugin.msgManager.getMsg(Lang.List2));
            } else if (s[0].equalsIgnoreCase("reload")) {
                plugin.reload();
                arg0.sendMessage(plugin.msgManager.getMsg(Lang.ReloadSuccess));
            }
        } else if (s.length == 2) {
            if (s[0].equalsIgnoreCase("list")) {
                List<Integer> I = plugin.getPlayerAllTitleID(s[1]);
                List<String> LS = plugin.getPlayerAllTitle(s[1]);
                arg0.sendMessage("§2--------§b[§6" + s[1] + "的称号" + "§b]§2-------");
                for (int o = 0; o < I.size(); o++) {
                    arg0.sendMessage("§4" + I.get(o) + "§b:---:" + LS.get(o));
                }
                arg0.sendMessage("§2--------§b[玩家称号列表]§2-------");
            }
        } else if (s.length == 3) {
            if (!arg0.hasPermission("udtitle.admin") && !(arg0 instanceof ConsoleCommandSender)) {
                return false;
            }
            if (s[0].equalsIgnoreCase("padd")) {
                if (!arg0.hasPermission("udtitle.admin")) {
                    return false;
                }
                if (plugin.addPlayerTitle(s[1], Integer.valueOf(s[2]).intValue())) {
                    arg0.sendMessage("§a添加成功");
                } else {
                    arg0.sendMessage("§c添加失败");
                }
            } else if (s[0].equalsIgnoreCase("prm")) {
                if (!arg0.hasPermission("udtitle.admin")) {
                    return false;
                }
                if (plugin.removePlayerTitle(p.getName(), Integer.valueOf(s[2]).intValue())) {
                    arg0.sendMessage("§a删除成功");
                } else {
                    arg0.sendMessage("§c删除失败");
                }
            } else if (s[0].equalsIgnoreCase("pset")) {
                if (arg0 instanceof Player) {
                    if (arg0.getName().equalsIgnoreCase(s[1])) {
                        if (!plugin.isTitleExist(Integer.valueOf(s[2]).intValue()).booleanValue()) {
                            arg0.sendMessage("§6称号不存在");
                            return true;
                        } else if (!plugin.setPlayerTitleUseMoney(p, Integer.valueOf(s[2]).intValue())) {
                            arg0.sendMessage("§6您的游戏币不足QAQ.无法更换称号");
                        } else {
                            arg0.sendMessage("§b已设置");
                        }
                    } else if (arg0.hasPermission("udtitle.admin")) {
                        plugin.setPlayerTitle(s[1], Integer.valueOf(s[2]).intValue());
                        arg0.sendMessage("§b已设置");
                    } else {
                        arg0.sendMessage("§c你没有权限设置其他人的称号");
                    }
                }
            }
        }
        return true;
    }
}
