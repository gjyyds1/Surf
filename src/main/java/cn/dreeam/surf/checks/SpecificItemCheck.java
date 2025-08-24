package cn.dreeam.surf.checks;

import cn.dreeam.surf.util.ItemUtil;
import org.bukkit.inventory.ItemStack;

/**
 * 特定物品类型检查类
 */
public class SpecificItemCheck extends BaseCheck {
    
    @Override
    public boolean isIllegal(ItemStack item) {
        if (item == null || ItemUtil.isAir(item)) {
            return false;
        }
        
        // 检查图腾堆叠
        if (ItemUtil.isIllegalTotem(item)) {
            return true;
        }
        
        // 检查可写书
        // 可以在这里添加书本相关的检查逻辑
        ItemUtil.isWritableBook(item);

        return false;
    }
    
    @Override
    public ItemStack clean(ItemStack item) {
        if (item == null || ItemUtil.isAir(item)) {
            return ItemStack.empty();
        }
        
        // 处理图腾堆叠问题
        if (ItemUtil.isIllegalTotem(item)) {
            item.setAmount(item.getMaxStackSize());
        }
        
        return item;
    }

}