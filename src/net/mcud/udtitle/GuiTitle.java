package net.mcud.udtitle;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiTitle {
	
    /* renamed from: 上一页  reason: contains not printable characters */
    public static String prevPage = "§b上一页";

    /* renamed from: 下一页  reason: contains not printable characters */
    public static String nextPage = "§b下一页";

    /* renamed from: 删除  reason: contains not printable characters */
    public static String cancelTag = "§c取消当前称号§2(设为默认)";
    public LanguageManager LM;
    
    Title plugin;
    public GuiTitle(Title plugin) {
    	this.plugin = plugin;
    }
    
    public void load() {
    }

    public void open(Player p, int index) {
        //List<Integer> GetPlayerAllTitleID = plugin.GetPlayerAllTitleID(p.getName());
        Inventory i = Bukkit.getServer().createInventory((InventoryHolder) null, 54, String.valueOf(plugin.titleGuiTitle) + plugin.packId(index) + "§r" + "§a-第" + index + "页");
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
        List<Integer> L = plugin.GetPlayerAllTitleID(Player.getName());
        List<String> S = plugin.getPlayerAllTitle(Player.getName());
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
                ItemStack IS = new ItemStack(M, 1, (short) plugin.GetTitleItemDataID(L.get(tid).intValue()));
                //IS.setData(D);
                //IS.setDurability((short) plugin.GetTitleItemDataID(L.get(tid).intValue()));
                
                ItemMeta IM = IS.getItemMeta();
                IM.setLore(plugin.GetLore(L.get(tid).intValue()));
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
        ItemStack IS4 = new ItemStack(Material.REDSTONE_TORCH, 1);
        IM2.setDisplayName(cancelTag);
        IS4.setItemMeta(IM2);
        I.setItem(49, IS4);
    }
}
