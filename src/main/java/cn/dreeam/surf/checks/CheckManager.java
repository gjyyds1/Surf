package cn.dreeam.surf.checks;

import cn.dreeam.surf.config.Config;
import cn.dreeam.surf.util.MessageUtil;
import cn.dreeam.surf.util.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查管理器，统一管理所有物品检查
 */
public class CheckManager {
    
    private static final List<BaseCheck> checks = new ArrayList<>();
    
    static {
        // 初始化所有检查类型
        checks.add(new GeneralCheck());
        checks.add(new SpecificItemCheck());
    }
    
    /**
     * 检查物品是否违法
     * @param item 要检查的物品
     * @return 如果物品违法返回true，否则返回false
     */
    public static boolean isIllegal(ItemStack item) {
        if (item == null || ItemUtil.isAir(item)) {
            return false;
        }
        
        for (BaseCheck check : checks) {
            if (check.isIllegal(item)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 清理物品的违法属性
     * @param item 要清理的物品
     * @return 清理后的物品
     */
    public static ItemStack cleanItem(ItemStack item) {
        if (item == null || ItemUtil.isAir(item)) {
            return ItemStack.empty();
        }
        
        ItemStack result = item.clone();
        
        for (BaseCheck check : checks) {
            result = check.clean(result);
            if (result == null || result.equals(ItemStack.empty())) {
                break;
            }
        }
        
        return result;
    }
    
    /**
     * 检查并清理背包中的违法物品
     * @param inventory 要检查的背包
     * @param playerName 玩家名称
     * @param bypassCheck 是否跳过检查（op权限）
     */
    public static void cleanInventory(Inventory inventory, String playerName, boolean bypassCheck) {
        if (bypassCheck) {
            return; // op跳过检查
        }
        
        ItemStack[] contents = inventory.getContents();
        
        if (contents.length == 0) return;
        
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item == null) continue;
            
            ItemStack original = item.clone();
            ItemStack cleaned = cleanItem(item);
            
            if (!original.equals(cleaned)) {
                if (cleaned.equals(ItemStack.empty())) {
                    inventory.setItem(i, null);
                } else {
                    inventory.setItem(i, cleaned);
                }
                
                MessageUtil.println(String.format(
                    "&6检测到违法物品 %s 在玩家 %s 的背包中",
                    ItemUtil.getItemDisplayName(original),
                    playerName
                ));
            }
        }
    }
    
    /**
     * 检查玩家是否可以跳过检测
     * @param player 玩家
     * @return 如果可以跳过返回true，否则返回false
     */
    public static boolean canBypassCheck(Player player) {
        return Config.opBypassEnabled && player.isOp();
    }
}