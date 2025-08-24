package cn.dreeam.surf.checks;

import org.bukkit.inventory.ItemStack;

/**
 * 基础检查类，所有物品检查都应继承此类
 */
public abstract class BaseCheck {
    
    /**
     * 检查物品是否违法
     * @param item 要检查的物品
     * @return 如果物品违法返回true，否则返回false
     */
    public abstract boolean isIllegal(ItemStack item);
    
    /**
     * 清理物品的违法属性
     * @param item 要清理的物品
     * @return 清理后的物品，如果物品应该被删除则返回null
     */
    public abstract ItemStack clean(ItemStack item);

}