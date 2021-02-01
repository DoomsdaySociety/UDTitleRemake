package net.mcud.udtitle;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
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
        //List<Integer> GetAllTitleID = plugin.GetAllTitleID();
        Inventory i = Bukkit.getServer().createInventory((InventoryHolder) null, 54, String.valueOf(plugin.listGuiTitle) + plugin.packId(index) + "§r" + "§a-第" + index + "页");
        p.closeInventory();
        update(i, p, index);
        p.openInventory(i);
    }

    @SuppressWarnings("deprecation")
	public void update(Inventory I, Player Player, int index) {
        Material M;
        String prefix = plugin.chatApi.getPlayerPrefix(Player);
        if (prefix == null) {
            prefix = "";
        }
        //int extractId = plugin.extractId(prefix);
        List<Integer> L = plugin.GetAllTitleID();
        List<String> S = plugin.GetAllTitle();
        int a = L.size() / 45;
        int b = L.size() % 45;
        if (b == 0) {
            b = 45;
        }
        int index2 = index - 1;
        if (index2 < a) {
            b = 45;
        }
        I.clear();
        if (L.size() > 0 && index2 <= a) {
            for (int i = 0; i < b; i++) {
                int tid = (index2 * 45) + i;
                if (Integer.valueOf(plugin.GetTitleItemID(L.get(tid).intValue())).intValue() < 1) {
                    M = Material.NAME_TAG;
                } else {
                    M = Material.getMaterial(plugin.GetTitleItemID(L.get(tid).intValue()));
                }
                //MaterialData D = new MaterialData(plugin.GetTitleItemDataID(L.get(tid).intValue()));
                ItemStack IS = new ItemStack(M, 1, (short)plugin.GetTitleItemDataID(L.get(tid).intValue()));
                //IS.setData(D);
                ItemMeta IM = IS.getItemMeta();
                List<String> Lore = plugin.GetLore(L.get(tid).intValue());
                if (plugin.hasTitle(Player.getName(), tid)) {
                    Lore.add(0, plugin.msgManager.getMsg(Lang.hava));
                } else {
                    Lore.add(0, plugin.msgManager.getMsg(Lang.nohava));
                }
                IM.setLore(Lore);
                IM.setDisplayName(String.valueOf(plugin.packId(L.get(tid).intValue())) + "§r" + S.get(tid));
                IS.setItemMeta(IM);
                I.setItem(i, IS);
            }
        }
        ItemStack IS2 = new ItemStack(Material.PAPER, 1);
        ItemMeta IM2 = IS2.getItemMeta();
        IM2.setDisplayName(prevPage);
        IS2.setItemMeta(IM2);
        I.setItem(45, IS2);
        ItemStack IS3 = new ItemStack(Material.PAPER, 1);
        IM2.setDisplayName(nextPage);
        IS3.setItemMeta(IM2);
        I.setItem(53, IS3);
    }
}
