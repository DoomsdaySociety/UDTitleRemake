package net.mcud.udtitle;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ListGui {

    public static String prevPage = "§b上一页";

    public static String nextPage = "§b下一页";
    Title plugin;
    public ListGui(Title plugin) {
    	this.plugin = plugin;
    }

    public void load() {
    }

    public void open(Player p, int index) {
        //List<Integer> GetAllTitleID = plugin.getAllTitleID();
        Inventory i = Bukkit.getServer().createInventory(null, 54, plugin.listGuiTitle + plugin.packId(index) + "§r" + "§a-第" + index + "页");
        p.closeInventory();
        update(i, p, index);
        p.openInventory(i);
    }

	public void update(Inventory inv, Player player, int index) {
        String prefix = plugin.data.getPlayerTag(player.getName());
        if (prefix == null) {
            prefix = "";
        }
        //int extractId = plugin.extractId(prefix);
        List<Integer> listAllTitleID = plugin.getAllTitleID();
        List<String> listAllTitle = plugin.getAllTitle();
        int a = listAllTitleID.size() / 45;
        int b = listAllTitleID.size() % 45;
        if (b == 0) {
            b = 45;
        }
        int index2 = index - 1;
        if (index2 < a) {
            b = 45;
        }
        inv.clear();
        if (listAllTitleID.size() > 0 && index2 <= a) {
            for (int i = 0; i < b; i++) {
                int tid = (index2 * 45) + i;
                Material M = Title.valueOf(plugin.getTitleItemID(listAllTitleID.get(tid).intValue()), Material.NAME_TAG);
                //MaterialData D = new MaterialData(plugin.getTitleItemDataID(L.get(tid).intValue()));
                ItemStack IS = new ItemStack(M, 1);
                //IS.setData(D);
                ItemMeta IM = IS.getItemMeta();
                List<String> Lore = plugin.getLore(listAllTitleID.get(tid).intValue());
                if (plugin.hasTitle(player.getName(), tid)) {
                    Lore.add(0, plugin.msgManager.getMsg(Lang.hava));
                } else {
                    Lore.add(0, plugin.msgManager.getMsg(Lang.nohava));
                }
                IM.setLore(Lore);
                IM.setDisplayName(String.valueOf(plugin.packId(listAllTitleID.get(tid).intValue())) + "§r" + listAllTitle.get(tid).replace("&", "§"));
                IS.setItemMeta(IM);
                inv.setItem(i, IS);
            }
        }
        ItemStack IS2 = new ItemStack(Material.PAPER, 1);
        ItemMeta IM2 = IS2.getItemMeta();
        IM2.setDisplayName(prevPage);
        IS2.setItemMeta(IM2);
        inv.setItem(45, IS2);
        ItemStack IS3 = new ItemStack(Material.PAPER, 1);
        IM2.setDisplayName(nextPage);
        IS3.setItemMeta(IM2);
        inv.setItem(53, IS3);
    }
}
