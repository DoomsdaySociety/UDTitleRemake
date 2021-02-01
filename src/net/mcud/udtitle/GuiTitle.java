package net.mcud.udtitle;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
        //List<Integer> GetPlayerAllTitleID = plugin.getPlayerAllTitleID(p.getName());
        Inventory i = Bukkit.getServer().createInventory(null, 54, plugin.titleGuiTitle + plugin.packId(index) + "§r" + "§a-第" + index + "页");
        
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
        List<Integer> listAllTitleID = plugin.getPlayerAllTitleID(player.getName());
        List<String> listAllTitle = plugin.getPlayerAllTitle(player.getName());
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
                //IS.setDurability((short) plugin.getTitleItemDataID(L.get(tid).intValue()));
                
                ItemMeta IM = IS.getItemMeta();
                IM.setLore(plugin.getLore(listAllTitleID.get(tid).intValue()));
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
        ItemStack IS4 = new ItemStack(Material.REDSTONE_TORCH, 1);
        IM2.setDisplayName(cancelTag);
        IS4.setItemMeta(IM2);
        inv.setItem(49, IS4);
    }
}
