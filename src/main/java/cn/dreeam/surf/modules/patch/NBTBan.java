package cn.dreeam.surf.modules.patch;

import cn.dreeam.surf.config.Config;
import cn.dreeam.surf.checks.CheckManager;
import cn.dreeam.surf.util.MessageUtil;
import cn.dreeam.surf.util.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class NBTBan implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!Config.preventNBTBanEnabled) return;
        
        Player player = event.getPlayer();
        // 检查OP跳过权限
        if (CheckManager.canBypassCheck(player)) return;

        checkInventoryForNBTBan(player.getInventory(), player);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!Config.preventNBTBanEnabled || !(event.getWhoClicked() instanceof Player)) return;
        
        Player player = (Player) event.getWhoClicked();
        // 检查OP跳过权限
        if (CheckManager.canBypassCheck(player)) return;
        
        ItemStack item = event.getCurrentItem();
        if (item != null && hasIllegalNBT(item)) {
            event.setCancelled(true);
            MessageUtil.sendMessage(player, Config.preventNBTBanMessage);
        }
    }
    
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!Config.preventNBTBanEnabled) return;
        
        Player player = event.getPlayer();
        // 检查OP跳过权限
        if (CheckManager.canBypassCheck(player)) return;
        
        ItemStack item = event.getItemDrop().getItemStack();
        if (hasIllegalNBT(item)) {
            event.setCancelled(true);
            MessageUtil.sendMessage(player, Config.preventNBTBanMessage);
        }
    }
    
    /**
     * 检查背包中的NBT违法物品
     */
    private void checkInventoryForNBTBan(Inventory inventory, Player player) {
        for (ItemStack item : inventory) {
            if (item != null && hasIllegalNBT(item)) {
                inventory.remove(item);
                MessageUtil.sendMessage(player, Config.preventNBTBanMessage);
            }
        }
    }
    
    /**
     * 检查物品是否有违法的NBT数据
     */
    private boolean hasIllegalNBT(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        
        // 主要检查潜影盒和其他容器物品
        if (item.getType().name().contains("SHULKER_BOX") || 
            item.getType().name().contains("CHEST") ||
            item.getType().name().contains("BARREL")) {
            
            try {
                int itemSize;
                if (Util.isNewerAndEqual(20, 5)) {
                    itemSize = item.getItemMeta().getAsComponentString().length();
                } else {
                    itemSize = item.getItemMeta().getAsString().length();
                }
                
                return itemSize > Config.preventNBTBanLimit;
            } catch (Exception e) {
                // 如果获取NBT数据时出现异常，认为是违法物品
                return true;
            }
        }
        
        return false;
    }
}
