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
	
    public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
        Player p = null;
        
        if (sender instanceof Player) {
            p = (Player) sender;
        }
        if (args.length == 0) {
            if (!plugin.isUseGui) {
                sender.sendMessage(plugin.msgManager.getMsg(Lang.CANNOTOPENGUI));
                return true;
            }
            plugin.getGuiTitle().open(p, 1);
        } else if (args.length == 1) {
        	if(args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(plugin.msgManager.getMsg(Lang.HELP1));
                sender.sendMessage(plugin.msgManager.getMsg(Lang.HELP2));
                sender.sendMessage(plugin.msgManager.getMsg(Lang.HELP3));
        	}
        	else if (args[0].equalsIgnoreCase("open")) {
                if (!plugin.isUseGui) {
                    sender.sendMessage(plugin.msgManager.getMsg(Lang.CANNOTOPENGUI));
                    return true;
                }
                plugin.getGuiTitle().open(p, 1);
            } else if (args[0].equalsIgnoreCase("list")) {
                if (sender instanceof Player) {
                	plugin.getGuiList().open(p, 1);
                    return true;
                }
                sender.sendMessage(plugin.msgManager.getMsg(Lang.LIST1));
                
                for(Integer id : plugin.titleMap.keySet()) {
                    sender.sendMessage("§4" + id + "§b:---:" + plugin.titleMap.get(id));
                }
                sender.sendMessage(plugin.msgManager.getMsg(Lang.LIST2));
            } else if (args[0].equalsIgnoreCase("reload")) {
                plugin.reload();
                sender.sendMessage(plugin.msgManager.getMsg(Lang.RELOADSUCCESS));
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("list")) {
                List<Integer> I = plugin.getPlayerAllTitleID(args[1]);
                List<String> LS = plugin.getPlayerAllTitle(args[1]);
                sender.sendMessage("§2--------§b[§6" + args[1] + "的称号" + "§b]§2-------");
                for (int o = 0; o < I.size(); o++) {
                    sender.sendMessage("§4" + I.get(o) + "§b:---:" + LS.get(o));
                }
                sender.sendMessage("§2--------§b[玩家称号列表]§2-------");
            }
        } else if (args.length == 3) {
            if (!sender.hasPermission("udtitle.admin") && !(sender instanceof ConsoleCommandSender)) {
                return false;
            }
            if (args[0].equalsIgnoreCase("padd")) {
                if (!sender.hasPermission("udtitle.admin")) {
                    return false;
                }
                if (plugin.addPlayerTitle(args[1], Integer.valueOf(args[2]).intValue())) {
                    sender.sendMessage("§a添加成功");
                } else {
                    sender.sendMessage("§c添加失败");
                }
            } else if (args[0].equalsIgnoreCase("prm")) {
                if (!sender.hasPermission("udtitle.admin")) {
                    return false;
                }
                if (plugin.removePlayerTitle(p.getName(), Integer.valueOf(args[2]).intValue())) {
                    sender.sendMessage("§a删除成功");
                } else {
                    sender.sendMessage("§c删除失败");
                }
            } else if (args[0].equalsIgnoreCase("pset")) {
                if (sender instanceof Player) {
                    if (sender.getName().equalsIgnoreCase(args[1])) {
                        if (!plugin.isTitleExist(Integer.valueOf(args[2]).intValue()).booleanValue()) {
                            sender.sendMessage("§6称号不存在");
                            return true;
                        } else if (!plugin.setPlayerTitleUseMoney(p, Integer.valueOf(args[2]).intValue())) {
                            sender.sendMessage("§6您的游戏币不足QAQ.无法更换称号");
                        } else {
                            sender.sendMessage("§b已设置");
                        }
                    } else if (sender.hasPermission("udtitle.admin")) {
                        plugin.setPlayerTitle(args[1], Integer.valueOf(args[2]).intValue());
                        sender.sendMessage("§b已设置");
                    } else {
                        sender.sendMessage("§c你没有权限设置其他人的称号");
                    }
                }
            }
        }
        return true;
    }
}
